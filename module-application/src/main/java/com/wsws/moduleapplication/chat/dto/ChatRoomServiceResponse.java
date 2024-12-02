package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.vo.ChatMessageInfraDto;
import com.wsws.moduledomain.chat.vo.ChatRoomInfraDto;
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
    public ChatRoomServiceResponse(ChatRoomInfraDto chatRoom, User otherUser, ChatMessageInfraDto lastMessage, Long unreadMessageCount) {
        this(
                chatRoom.id(),
                otherUser.getNickname().getValue(),
                otherUser.getProfileImage(),
                lastMessage != null ? lastMessage.content() : null,
                lastMessage != null ? lastMessage.createdAt() : null,
                unreadMessageCount
        );
    }
}