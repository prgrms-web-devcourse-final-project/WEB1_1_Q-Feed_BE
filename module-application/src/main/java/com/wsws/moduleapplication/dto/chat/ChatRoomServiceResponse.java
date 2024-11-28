package com.wsws.moduleapplication.dto.chat;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomServiceResponse(Long chatRoomId, String user2Nickname, String lastMessage, LocalDateTime lastMessageTime) {

    public static ChatRoomServiceResponse fromEntity(ChatRoom chatRoom, String userId) {

        String otherUserId = chatRoom.getUserId().equals(userId) ? chatRoom.getUserId2() : chatRoom.getUserId();

        return new ChatRoomServiceResponse(
                chatRoom.getId(),
                otherUserId,
               "마지막메세지",
                chatRoom.getLastMessageTime()
        );
    }
}