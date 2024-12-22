package com.wsws.moduleapplication.chat.service;

import com.wsws.moduleapplication.chat.dto.ChatMessageRequest;
import com.wsws.moduleapplication.chat.dto.ChatMessageServiceResponse;
import com.wsws.moduleapplication.chat.exception.ChatRoomNotFoundException;
import com.wsws.moduleapplication.chat.exception.FileProcessingException;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduleapplication.util.FileValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.modulecommon.service.RedisService;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatMessageDomainResponse;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.MessageType;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduledomain.chat.dto.ChatMessageDTO;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleinfra.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Transactional
    public void sendMessage(Long chatRoomId, String senderId, ChatMessageRequest request ) {
        validateChatRoom(chatRoomId);
        User user = validateUser(senderId);

        //String fileProcess = processFile(request.file(),request.type());

        // 메시지 생성
        ChatMessage chatMessage = createChatMessage(chatRoomId, senderId, request);

        //db에 메세지 저장(비동기)
        chatPersistenceService.saveMessageInRedisAsync(chatRoomId,chatMessage);

        //구독 및 redis 발행
        chatWebSocketService.notifyWebSocketSubscribers(chatRoomId, chatMessage, user);
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

    //메세지 생성
    private ChatMessage createChatMessage(Long chatRoomId, String senderId, ChatMessageRequest request) {
        return ChatMessage.create(
                null,
                request.content(),
                request.type(),
                request.file(),
                false,
                LocalDateTime.now(),
                senderId,
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

    //이미지 or 음성 처리
    private String processFile(MultipartFile file, MessageType type) {
        System.out.println("!11111111");
        if (file != null && !file.isEmpty()) {
            try {
                // 타입에 따른 파일 검증 및 저장 처리
                switch (type) {
                    case IMAGE -> {
                        FileValidator.validate(file,"image");
                        System.out.println("!222222222222");
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
