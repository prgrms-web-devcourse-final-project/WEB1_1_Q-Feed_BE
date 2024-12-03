package com.wsws.moduledomain.chat.repo;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.dto.ChatMessageDTO;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository {
    void save(ChatMessage chatMessage);

    void markAllMessagesAsRead(Long chatRoomId);

    List<ChatMessageDTO> findMessagesWithUserDetails(Long chatRoomId, int page, int size);
    // 특정 채팅방에서 마지막 메시지를 가져오기 (최신 메시지 하나만)
    Optional<ChatMessage> findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

    long countUnreadMessages(Long chatRoomId,String userId);
}
