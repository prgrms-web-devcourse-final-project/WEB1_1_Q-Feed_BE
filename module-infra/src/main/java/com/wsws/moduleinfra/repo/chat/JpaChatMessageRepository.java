package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.vo.ChatMessageDTO;
import com.wsws.moduleinfra.entity.chat.ChatMessageEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface JpaChatMessageRepository extends JpaRepository<ChatMessageEntity, Long>, ChatMessageRepository {

    @Query("SELECT new com.wsws.moduledomain.chat.vo.ChatMessageDTO(" +
            "m.id, m.content, m.type, m.url, m.isRead, m.createdAt, u.id.value, u.nickname.value, u.profileImage) " +
            "FROM ChatMessageEntity m " +
            "JOIN User u ON m.userId = u.id " +
            "WHERE m.chatRoom.id = :chatRoomId " +
            "ORDER BY m.createdAt DESC")
    List<ChatMessageDTO> findMessagesWithUserDetails(@Param("chatRoomId") Long chatRoomId, int page, int size);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessageEntity cm SET cm.isRead = true WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false")
    void markAllMessagesAsRead(@Param("chatRoomId") Long chatRoomId);

    // 특정 채팅방에서 마지막 메시지를 가져오기 (최신 메시지 하나만)
    Optional<ChatMessage> findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
