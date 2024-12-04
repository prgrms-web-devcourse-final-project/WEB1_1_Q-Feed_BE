package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.dto.ChatMessageDTO;
import com.wsws.moduleinfra.entity.chat.ChatMessageEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface JpaChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    @Query("SELECT new com.wsws.moduledomain.chat.dto.ChatMessageDTO(" +
            "m.id, m.content, m.type, m.url, m.isRead, m.createdAt, u.id, u.nickname, u.profileImage) " +
            "FROM ChatMessageEntity m " +
            "JOIN UserEntity u ON m.userId = u.id " +
            "WHERE m.chatRoom.id = :chatRoomId " +
            "ORDER BY m.createdAt")
    Page<ChatMessageDTO> findMessagesWithUserDetails(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessageEntity cm SET cm.isRead = true WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false")
    void markAllMessagesAsRead(@Param("chatRoomId") Long chatRoomId);

    // 특정 채팅방에서 마지막 메시지를 가져오기 (최신 메시지 하나만)
    Optional<ChatMessageEntity> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    @Query("SELECT COUNT(cm) FROM ChatMessageEntity cm WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false AND cm.userId = :userId")
    long countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("userId") String userId);
}
