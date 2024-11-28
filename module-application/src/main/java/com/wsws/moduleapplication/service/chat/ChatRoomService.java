package com.wsws.moduleapplication.service.chat;

import com.wsws.moduleapplication.dto.chat.ChatRoomServiceRequest;
import com.wsws.moduleapplication.dto.chat.ChatRoomServiceResponse;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduleinfra.repo.chat.JpaChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository  chatRoomRepository;

    public void createChatRoom(ChatRoomServiceRequest req) {
        // 기존 채팅방이 존재하는지 확인
        if (chatRoomRepository.findChatRoomBetweenUsers(req.userId(), req.userId2()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 채팅방이 있습니다.");
        }
        ChatRoom chatRoom = ChatRoom.create(req.userId(), req.userId2());
        chatRoomRepository.save(chatRoom);
    }

    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 채팅방이 존재하지 않습니다."));
        chatRoomRepository.delete(chatRoom);
    }

    public List<ChatRoomServiceResponse> getChatRooms(String userId){
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserIdOrderedByLastMessageTime(userId);
        // DTO 변환
        return chatRooms.stream()
                .map(chatRoom -> ChatRoomServiceResponse.fromEntity(chatRoom, userId))
                .collect(Collectors.toList());
    }

    public ChatRoomServiceResponse getChatRoomWithOtherUser(String userId, String userId2) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomBetweenUsers(userId, userId2)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자와의 채팅방이 존재하지 않습니다."));
        // DTO 변환
        return ChatRoomServiceResponse.fromEntity(chatRoom, userId);

    }

}

