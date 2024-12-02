package com.wsws.moduleapplication.chat.service;

import com.wsws.moduleapplication.chat.dto.ChatRoomServiceRequest;
import com.wsws.moduleapplication.chat.dto.ChatRoomServiceResponse;
import com.wsws.moduleapplication.chat.exception.AlreadyChatRoomException;
import com.wsws.moduleapplication.chat.exception.ChatRoomNotFoundException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduledomain.chat.vo.ChatMessageInfraDto;
import com.wsws.moduledomain.chat.vo.ChatRoomInfraDto;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.UserId;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.wsws.moduleapplication.chat.exception.ChatServiceErrorCode.CHATROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository  chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createChatRoom(ChatRoomServiceRequest req) {
        // 기존 채팅방이 존재하는지 확인
        if (chatRoomRepository.findChatRoomBetweenUsers(req.userId(), req.userId2()).isPresent()) {
            throw AlreadyChatRoomException.EXCEPTION;
        }
        ChatRoom chatRoom = ChatRoom.create(req.userId(), req.userId2());
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoomInfraDto chatRoom = getChatRoomById(chatRoomId);
        chatRoomRepository.delete(chatRoom);
    }

    //채팅방 목록 조회
    public List<ChatRoomServiceResponse> getChatRooms(String userId) {
        // 채팅방 목록을 가져옴
        List<ChatRoomInfraDto> chatRooms = chatRoomRepository.findChatRooms(userId);

        return chatRooms.stream().map(chatRoom -> {
            // 상대방 사용자 가져오기
            User otherUser = getOtherUser(chatRoom, userId);

            // 마지막 메시지 가져오기
            ChatMessageInfraDto lastMessage = getLastMessage(chatRoom);

            // 읽지 않은 메시지 개수 가져오기
            long unreadCount = chatMessageRepository.countUnreadMessages(chatRoom.id(), otherUser.getId().getValue());

            return new ChatRoomServiceResponse(
                    chatRoom, otherUser, lastMessage, unreadCount
            );
        }).collect(Collectors.toList());
    }

    //채팅방 찾기
    public ChatRoomServiceResponse getChatRoomWithOtherUser(String userId, String userId2) {
        ChatRoomInfraDto chatRoom = findChatRoomBetweenUsers(userId, userId2);

        User otherUser = getUserById(userId2);

        // 마지막 메시지 가져오기
        ChatMessageInfraDto lastMessage = getLastMessage(chatRoom);

        // 읽지 않은 메시지 개수 가져오기
        long unreadCount = chatMessageRepository.countUnreadMessages(chatRoom.id(), otherUser.getId().getValue());

        return new ChatRoomServiceResponse(
                chatRoom, otherUser, lastMessage, unreadCount
        );
    }

    private ChatRoomInfraDto getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findChatRoomById(chatRoomId)
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private ChatRoomInfraDto findChatRoomBetweenUsers(String userId, String userId2) {
        return chatRoomRepository.findChatRoomBetweenUsers(userId, userId2)
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private User getUserById(String senderId) {
        return userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    // 다른 사용자의 정보 가져오기
    private User getOtherUser(ChatRoomInfraDto chatRoom, String userId) {
        String otherUserId = chatRoom.userId().equals(userId) ? chatRoom.userId2() : chatRoom.userId();
        return getUserById(otherUserId);
    }

    // 마지막 메시지 가져오기
    private ChatMessageInfraDto getLastMessage(ChatRoomInfraDto chatRoom) {
        return chatMessageRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom)
                .orElse(null);  // 메시지가 없으면 null 반환
    }


}

