package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.vo.ChatMessageUserInfraDTO;
import com.wsws.moduledomain.chat.vo.ChatMessageInfraDto;
import com.wsws.moduledomain.chat.vo.ChatMessageUserInfraDTO;
import com.wsws.moduledomain.chat.vo.ChatRoomInfraDto;
import com.wsws.moduleinfra.entity.chat.ChatMessageEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface JpaChatMessageRepository extends JpaRepository<ChatMessageEntity, Long>, ChatMessageRepository {

    @Query("SELECT new com.wsws.moduledomain.chat.vo.ChatMessageUserInfraDTO(" +
            "m.id, m.content, m.type, m.url, m.isRead, m.createdAt, u.id.value, u.nickname.value, u.profileImage) " +
            "FROM ChatMessageEntity m " +
            "JOIN User u ON m.userId = u.id " +
            "WHERE m.chatRoom.id = :chatRoomId " +
            "ORDER BY m.createdAt DESC")
    List<ChatMessageUserInfraDTO> findMessagesWithUserDetails(@Param("chatRoomId") Long chatRoomId, int page, int size);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessageEntity cm SET cm.isRead = true WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false")
    void markAllMessagesAsRead(@Param("chatRoomId") Long chatRoomId);

    // 특정 채팅방에서 마지막 메시지를 가져오기 (최신 메시지 하나만)
    @Query("SELECT new com.wsws.moduledomain.chat.vo.ChatMessageInfraDto(cm.id,cm.content,cm.type,cm.url,cm.isRead,cm.createdAt,cm.userId,cm.chatRoom.id) " +
            "FROM ChatMessageEntity cm " +
            "WHERE cm.chatRoom = :chatRoom " +
            "ORDER BY cm.createdAt DESC")
    Optional<ChatMessageInfraDto> findTopByChatRoomOrderByCreatedAtDesc(ChatRoomInfraDto chatRoom);

    @Query("SELECT COUNT(cm) FROM ChatMessageEntity cm WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false AND cm.userId = :userId")
    long countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("userId") String userId);
}
