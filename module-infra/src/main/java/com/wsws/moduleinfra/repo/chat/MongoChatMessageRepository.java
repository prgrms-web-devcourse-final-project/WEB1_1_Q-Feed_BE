package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduleinfra.repo.chat.dto.ChatMessageInfraDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface MongoChatMessageRepository extends JpaRepository<ChatMessage, String>, ChatMessageRepository {

    @Query("SELECT new com.wsws.moduleinfra.repo.chat.dto.ChatMessageInfraDTO(" +
            "m.id, m.content, m.type, m.url, m.isRead, m.createdAt,u.id, u.nickname, u.profileImage) " +
            "FROM ChatMessage m " +
            "JOIN User u ON m.userId = u.id " +
            "WHERE m.chatRoom.id = :chatRoomId " +
            "ORDER BY m.createdAt DESC")
    List<ChatMessageInfraDTO> findMessagesWithUserDetails(@Param("chatRoomId") Long chatRoomId,Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage cm SET cm.isRead = true WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false")
    void markAllMessagesAsRead(@Param("chatRoomId") Long chatRoomId);

    // 특정 채팅방에서 마지막 메시지를 가져오기 (최신 메시지 하나만)
    Optional<ChatMessage> findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
