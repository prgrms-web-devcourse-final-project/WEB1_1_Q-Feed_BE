package com.wsws.moduledomain.chat.repo;

import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.vo.ChatRoomInfraDto;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);

    void delete(ChatRoomInfraDto chatRoom);

    Optional<ChatRoomInfraDto> findChatRoomBetweenUsers(String userId, String userId2);

    Optional<ChatRoomInfraDto> findChatRoomById(Long chatRoomId);

    List<ChatRoomInfraDto> findChatRooms(String userId);
}
