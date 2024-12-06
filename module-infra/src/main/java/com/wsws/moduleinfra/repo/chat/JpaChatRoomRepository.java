package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduleinfra.entity.chat.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {

    @Query("SELECT cr FROM ChatRoomEntity cr WHERE (cr.userId = :userId AND cr.userId2 = :userId2) OR (cr.userId = :userId2 AND cr.userId2 = :userId)")
    Optional<ChatRoomEntity> findChatRoomBetweenUsers(@Param("userId") String userId, @Param("userId2") String userId2);

    @Query("SELECT cr FROM ChatRoomEntity cr WHERE cr.userId = :userId OR cr.userId2 = :userId ORDER BY cr.createdAt DESC")
    List<ChatRoomEntity> findChatRooms(@Param("userId") String userId);

    Optional<ChatRoomEntity> findChatRoomById(Long chatRoomId);
}
