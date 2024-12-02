package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.vo.Content;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.vo.Nickname;
import org.apache.logging.log4j.message.Message;

import javax.swing.text.AbstractDocument;
import java.time.LocalDateTime;

public record ChatRoomServiceResponse(
        Long chatRoomId,
        String otherUserNickname,
        String otherUserProfile,
        String lastMessageContent,
        LocalDateTime lastMessageCreatedAt
) {
    public ChatRoomServiceResponse(ChatRoom chatRoom, User otherUser, ChatMessage lastMessage) {
        this(
                chatRoom.getId(),
                otherUser.getNickname().getValue(),
                otherUser.getProfileImage(),
                lastMessage != null ? lastMessage.getContent().getValue() : null,
                lastMessage != null ? lastMessage.getCreatedAt() : null
        );
    }
}