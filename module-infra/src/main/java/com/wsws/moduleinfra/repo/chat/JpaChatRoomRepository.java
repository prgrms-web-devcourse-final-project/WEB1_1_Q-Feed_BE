package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaChatRoomRepository extends JpaRepository<ChatRoom, Long>,ChatRoomRepository {

    // 1. userId와 userId2가 일치하는 채팅방 찾기 (순서에 상관없이)
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.userId = :userId AND cr.userId2 = :userId2) OR (cr.userId = :userId2 AND cr.userId2 = :userId)")
    Optional<ChatRoom> findChatRoomBetweenUsers(@Param("userId") String userId, @Param("userId2") String userId2);


    List<ChatRoom> findByUserIdOrUserId2(String userId, String userId1);

    // userId 또는 userId2가 일치하는 채팅방을 시간 순으로 정렬해서 가져오기
    @Query("SELECT cr FROM ChatRoom cr " +
            "LEFT JOIN ChatMessage cm ON cr.id = cm.chatRoom.id " +
            "WHERE cr.userId = :userId OR cr.userId2 = :userId " +
            "ORDER BY cm.createdAt DESC")
    List<ChatRoom> findChatRoomsByUserIdOrderedByLastMessageTime(String userId);
}
