package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.user.User;

import java.time.LocalDateTime;

public record ChatRoomServiceResponse(
        Long chatRoomId,
        String otherUserNickname,
        String otherUserProfile,
        String lastMessageContent,
        LocalDateTime lastMessageCreatedAt,
        Long unreadMessageCount
) {
    public ChatRoomServiceResponse(ChatRoom chatRoom, User otherUser, ChatMessage lastMessage, Long unreadMessageCount) {
        this(
                chatRoom.getId(),
                otherUser.getNickname().getValue(),
                otherUser.getProfileImage(),
                lastMessage != null ? lastMessage.getContent().getValue() : null,
                lastMessage != null ? lastMessage.getCreatedAt() : null,
                unreadMessageCount
        );
    }
}