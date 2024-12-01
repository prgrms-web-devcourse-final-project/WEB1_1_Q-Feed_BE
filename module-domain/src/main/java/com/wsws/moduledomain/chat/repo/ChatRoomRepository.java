package com.wsws.moduledomain.chat.repo;

import com.wsws.moduledomain.chat.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);

    void delete(ChatRoom chatRoom);

    Optional<ChatRoom> findChatRoomBetweenUsers(String userId, String userId2);

    Optional<ChatRoom> findById(Long chatRoomId);

    List<ChatRoom> findChatRooms(String userId);
}