package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.dto.ChatMessageDTO;
import com.wsws.moduleinfra.entity.chat.ChatMessageEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    @Query("SELECT new com.wsws.moduledomain.chat.dto.ChatMessageDTO(" +
            "m.id, m.content, m.type, m.url, m.isRead, m.createdAt, u.id, u.nickname, u.profileImage) " +
            "FROM ChatMessageEntity m " +
            "JOIN UserEntity u ON m.userId = u.id " +
            "WHERE m.chatRoom.id = :chatRoomId " +
            "AND m.createdAt < :cursorCreatedAt " +
            "ORDER BY m.createdAt desc ")
    List<ChatMessageDTO> findMessagesWithUserDetails(@Param("chatRoomId") Long chatRoomId, @Param("cursorCreatedAt") LocalDateTime cursor, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessageEntity cm SET cm.isRead = true WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false")
    void markAllMessagesAsRead(@Param("chatRoomId") Long chatRoomId);

    // 특정 채팅방에서 마지막 메시지를 가져오기 (최신 메시지 하나만)
    Optional<ChatMessageEntity> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    @Query("SELECT COUNT(cm) FROM ChatMessageEntity cm WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false AND cm.userId = :userId")
    long countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("userId") String userId);
}
