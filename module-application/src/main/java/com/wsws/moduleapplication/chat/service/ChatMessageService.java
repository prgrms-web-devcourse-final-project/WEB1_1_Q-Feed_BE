package com.wsws.moduleapplication.chat.service;

import com.wsws.moduleapplication.chat.dto.ChatMessageRequest;
import com.wsws.moduleapplication.chat.dto.ChatMessageServiceResponse;
import com.wsws.moduleapplication.chat.event.SendMessageEvent;
import com.wsws.moduleapplication.chat.exception.ChatReceiverNotFoundException;
import com.wsws.moduleapplication.chat.exception.ChatRoomNotFoundException;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.modulecommon.service.RedisService;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduledomain.chat.dto.ChatMessageDTO;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import com.wsws.moduleinfra.repo.chat.mapper.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ChatWebSocketService chatWebSocketService;
    private final ChatPersistenceService chatPersistenceService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisService redisService;

    @Transactional
    public void sendMessage(Long chatRoomId, String senderId, String receiverId, ChatMessageRequest request ) {
        validateChatRoom(chatRoomId);
        User user = validateUser(senderId);
        validateUser(receiverId);

        //String fileProcess = processChatImage(request.file());

        boolean receiverInChatRoom = isReceiverInChatRoom(chatRoomId, receiverId);

        // 메시지 생성
        ChatMessage chatMessage = createChatMessage(chatRoomId, senderId, receiverId,receiverInChatRoom, request);

        //db에 메세지 저장(비동기)
        chatPersistenceService.saveMessageInRedisAsync(chatRoomId,chatMessage);

        //구독 및 redis 발행
        chatWebSocketService.notifyWebSocketSubscribers(chatRoomId, chatMessage, user);

        if(!receiverInChatRoom) {
            //알림보내기
            log.info("상대방이 접속해있지 않습니다. 알림을 보냅니다.");

            // 채팅 이벤트 발행
            eventPublisher.publishEvent(new SendMessageEvent(
                    senderId,
                    receiverId,
                    FcmType.CHAT
            ));
        }
    }

    //채팅방의 메세지 조회
    public List<ChatMessageServiceResponse> getChatMessages(Long chatRoomId,String userId, LocalDateTime cursor, int size ) {
        List<ChatMessageDTO> chatMessages = chatMessageRepository.findMessagesWithUserDetails(chatRoomId, cursor, size);

        // 메시지 소유 여부를 Map으로 반환
        Map<Long, Boolean> messageOwnershipMap = getMessageOwnershipMap(chatMessages, userId);

        return chatMessages.stream()
                .map(message -> new ChatMessageServiceResponse(message, messageOwnershipMap.get(message.messageId())))
                .collect(Collectors.toList());
    }

    // 메세지 읽음 처리
    public void markAllMessagesAsRead(Long chatRoomId) {
        chatMessageRepository.markAllMessagesAsRead(chatRoomId);
    }

    private boolean isReceiverInChatRoom(Long chatRoomId, String receiverId) {
        try{
            String userCurrentRoom = redisService.getUserCurrentRoom(receiverId);
            log.info("수신자 {}가 현재 채팅방 {}에 존재합니다.",receiverId,chatRoomId);

            boolean isInRoom = userCurrentRoom != null && userCurrentRoom.equals(chatRoomId.toString());
            log.info("userCurrentRoom.equals(chatRoomId.toString()): {}", isInRoom);
            return isInRoom;
        } catch (Exception e) {
            throw ChatReceiverNotFoundException.EXCEPTION;
        }
    }

    //메세지 생성
    private ChatMessage createChatMessage(Long chatRoomId, String senderId, String receiverId,boolean isReceiverIn,ChatMessageRequest request) {
        return ChatMessage.create(
                null,
                request.content(),
                request.type(),
                request.file(),
                isReceiverIn,
                LocalDateTime.now(),
                senderId,
                receiverId,
                chatRoomId
        );
    }

    // 메시지 소유 여부를 Map으로 반환
    private Map<Long, Boolean> getMessageOwnershipMap(List<ChatMessageDTO> chatMessages, String userId) {
        return chatMessages.stream()
                .collect(Collectors.toMap(
                        ChatMessageDTO::messageId,
                        message -> message.userId().equals(userId) // 메시지가 내 것인지 여부
                ));
    }

    // Unique 검사

    private ChatRoom validateChatRoom(Long chatRoomId) {
        return chatRoomRepository.findChatRoomById(chatRoomId)
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private User validateUser(String senderId) {
        return userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

//    private String processChatImage(byte[] imageData) {
//        if (imageData != null && imageData.length > 0) {
//            try {
//                // S3에 업로드하고 URL을 반환
//                return fileStorageService.saveFileByte(imageData);
//            } catch (Exception e) {
//                throw ProfileImageProcessingException.EXCEPTION;
//            }
//        }
//        return null;
//    }

}
