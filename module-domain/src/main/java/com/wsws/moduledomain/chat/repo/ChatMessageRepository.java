package com.wsws.moduledomain.chat.repo;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;

import java.util.Optional;

public interface ChatMessageRepository {
    ChatMessage save(ChatMessage chatMessage);

    void markAllMessagesAsRead(Long chatRoomId);

    // 특정 채팅방에서 마지막 메시지를 가져오기 (최신 메시지 하나만)
    Optional<ChatMessage> findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);
}
