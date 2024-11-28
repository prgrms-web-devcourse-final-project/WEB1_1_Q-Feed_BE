package com.wsws.moduledomain.chat.repo;

import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.user.vo.UserId;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {

    Optional<ChatRoom> findChatRoomBetweenUsers(@Param("userId") String userId, @Param("userId2") String userId2);

    List<ChatRoom> findChatRoomsByUserIdOrderedByLastMessageTime(String userId);

    void save(ChatRoom chatRoom);

    void delete(ChatRoom chatRoom);

    Optional<ChatRoom> findById(Long chatRoomId);

    List<ChatRoom> findByUserIdOrUserId2(String userId, String userId1);


}
