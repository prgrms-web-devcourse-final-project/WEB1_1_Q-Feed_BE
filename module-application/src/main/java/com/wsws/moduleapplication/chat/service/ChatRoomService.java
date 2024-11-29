package com.wsws.moduleapplication.chat.service;

import com.wsws.moduleapplication.chat.dto.ChatRoomServiceRequest;
import com.wsws.moduleapplication.chat.dto.ChatRoomServiceResponse;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.UserId;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new IllegalArgumentException("이미 존재하는 채팅방이 있습니다.");
        }
        ChatRoom chatRoom = ChatRoom.create(req.userId(), req.userId2());
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 채팅방이 존재하지 않습니다."));
        chatRoomRepository.delete(chatRoom);
    }

    //채팅방 목록 조회
    public List<ChatRoomServiceResponse> getChatRooms(String userId) {
        // 채팅방 목록을 가져옴
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRooms(userId);

        return chatRooms.stream().map(chatRoom -> {
            // 상대방 사용자 가져오기
            User otherUser = getOtherUser(chatRoom, userId);

            // 마지막 메시지 가져오기
            ChatMessage lastMessage = getLastMessage(chatRoom);

            return new ChatRoomServiceResponse(
                    chatRoom.getId(),
                    otherUser.getNickname(),
                    otherUser.getProfileImage(),
                    lastMessage != null ? lastMessage.getContent() : null,
                    lastMessage != null ? lastMessage.getCreatedAt() : null
            );
        }).collect(Collectors.toList());
    }

    //채팅방 찾기
    public ChatRoomServiceResponse getChatRoomWithOtherUser(String userId, String userId2) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomBetweenUsers(userId, userId2)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자와의 채팅방이 존재하지 않습니다."));

        User otherUser = getUserById(userId2);

        // 마지막 메시지 가져오기
        ChatMessage lastMessage = getLastMessage(chatRoom);

        return new ChatRoomServiceResponse(
                chatRoom.getId(),
                otherUser.getNickname(),
                otherUser.getProfileImage(),
                lastMessage != null ? lastMessage.getContent() : null,
                lastMessage != null ? lastMessage.getCreatedAt() : null
        );

    }

    private User getUserById(String senderId) {
        return userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 다른 사용자의 정보 가져오기
    private User getOtherUser(ChatRoom chatRoom, String userId) {
        String otherUserId = chatRoom.getUserId().equals(userId) ? chatRoom.getUserId2() : chatRoom.getUserId();
        return getUserById(otherUserId);
    }

    // 마지막 메시지 가져오기
    private ChatMessage getLastMessage(ChatRoom chatRoom) {
        return chatMessageRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom)
                .orElse(null);  // 메시지가 없으면 null 반환
    }


}

