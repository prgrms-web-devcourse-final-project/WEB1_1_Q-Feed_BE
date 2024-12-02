package com.wsws.moduleapplication.chat.service;

import com.wsws.moduleapplication.chat.dto.ChatMessageRequest;
import com.wsws.moduleapplication.chat.exception.ChatRoomNotFoundException;
import com.wsws.moduleapplication.chat.exception.FileProcessingException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduleapplication.util.FileValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.MessageType;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public void sendMessage(Long chatRoomId, String senderId, ChatMessageRequest request ) {
        ChatRoom chatRoom = getChatRoomById(chatRoomId);
        User user = getUserById(senderId);

        UserId userId = user.getId();

        // 이미지 or 음성 처리
        String fileProcess = processFile(request.file(),request.type());

        //메세지 생성
        ChatMessage chatMessage = ChatMessage.create(
                request.content(),
                request.type(),
                fileProcess,
                userId,
                chatRoom
        );
        chatMessageRepository.save(chatMessage);
    }

    // 채팅방의 메세지 조회
//    public List<ChatMessageInfraDTO> getMessagesByChatRoomId(Long chatRoomId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return chatMessageRepository.findMessagesWithUserDetails(chatRoomId,pageable);
//    }

    // 메세지 읽음 처리
    public void markAllMessagesAsRead(Long chatRoomId) {
        chatMessageRepository.markAllMessagesAsRead(chatRoomId);
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private User getUserById(String senderId) {
        return userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    //이미지 or 음성 처리
    private String processFile(MultipartFile file, MessageType type) {
        if (file != null && !file.isEmpty()) {
            try {
                // 타입에 따른 파일 검증 및 저장 처리
                switch (type) {
                    case IMAGE -> {
                        FileValidator.validate(file,"image");
                        return fileStorageService.saveFile(file, "images");
                    }
                    case AUDIO -> {
                        FileValidator.validate(file,"audio");
                        return fileStorageService.saveFile(file, "audios");
                    }
                }
            } catch (Exception e) {
                throw FileProcessingException.EXCEPTION;
            }
        }
        return null;
    }
}
