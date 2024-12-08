package com.wsws.moduleapplication.chat.service;

import com.wsws.moduleapplication.chat.dto.ChatMessageRequest;
import com.wsws.moduleapplication.chat.dto.ChatMessageServiceResponse;
import com.wsws.moduleapplication.chat.exception.ChatRoomNotFoundException;
import com.wsws.moduleapplication.chat.exception.FileProcessingException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduleapplication.util.FileValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatMessageDomainResponse;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.MessageType;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduledomain.chat.dto.ChatMessageDTO;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduleinfra.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisSubscriber redisSubscriber;

    @Transactional
    public void sendMessage(Long chatRoomId, String senderId,  ChatMessageRequest request ) {
        ChatRoom chatRoom = getChatRoomById(chatRoomId);
        User user = getUserById(senderId);
        UserId userId = user.getId();

//        // 이미지 or 음성 처리
//        String fileProcess = processFile(request.file(),request.type());

        //메세지 생성
        ChatMessage chatMessage = ChatMessage.create(
                null,
                request.content(),
                MessageType.TEXT,
                null,
                false,
                LocalDateTime.now(),
                senderId,
                chatRoomId
        );
        chatMessageRepository.save(chatMessage);

        // 해당 채팅방을 구독하도록 RedisSubscriber에 요청
        redisSubscriber.subscribeToChatRoom(chatRoomId);
        System.out.println("@@@@@@@@@@@@subscribeToChatRoom!!!!!");

        // Redis 발행
        ChatMessageDomainResponse response = ChatMessageDomainResponse.createFrom(chatMessage, user);
        String channel = "/sub/chat/" + chatRoomId;

        redisTemplate.convertAndSend(channel, response);
        System.out.println("@@@@@@@@@@@@convertAndSend!!!!!");

    }

    //채팅방의 메세지 조회
    public List<ChatMessageServiceResponse> getChatMessages(Long chatRoomId,String userId, LocalDateTime cursor, int size ) {
        List<ChatMessageDTO> chatMessages = chatMessageRepository.findMessagesWithUserDetails(chatRoomId, cursor, size);

        // 메시지 소유 여부를 Map으로 반환
        Map<Long, Boolean> messageOwnershipMap = getMessageOwnershipMap(chatMessages, userId);

        // ChatMessageDTO를 ChatMessageServiceResponse로 변환
        return chatMessages.stream()
                .map(message -> new ChatMessageServiceResponse(message, messageOwnershipMap.get(message.messageId())))
                .collect(Collectors.toList());
    }


    // 메세지 읽음 처리
    public void markAllMessagesAsRead(Long chatRoomId) {
        chatMessageRepository.markAllMessagesAsRead(chatRoomId);
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findChatRoomById(chatRoomId)
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private User getUserById(String senderId) {
        return userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    // 메시지 소유 여부를 Map으로 반환하는 함수
    private Map<Long, Boolean> getMessageOwnershipMap(List<ChatMessageDTO> chatMessages, String userId) {
        return chatMessages.stream()
                .collect(Collectors.toMap(
                        ChatMessageDTO::messageId,  // 메시지 ID를 key로 사용
                        message -> message.userId().equals(userId) // 메시지가 내 것인지 여부
                ));
    }


    //이미지 or 음성 처리
    private String processFile(MultipartFile file, MessageType type) {
        if (file != null && !file.isEmpty()) {
            try {
                // 타입에 따른 파일 검증 및 저장 처리
                switch (type) {
                    case IMAGE -> {
                        FileValidator.validate(file,"image");
                        return fileStorageService.saveFile(file);
                    }
                    case AUDIO -> {
                        FileValidator.validate(file,"audio");
                        return fileStorageService.saveFile(file);
                    }
                }
            } catch (Exception e) {
                throw FileProcessingException.EXCEPTION;
            }
        }
        return null;
    }
}
