package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.user.User;

public record ChatMessageServiceResponse(
        Long messageId,
        Long chatRoomId,
        String senderId,
        String senderName,
        String senderProfileImage,
        String content,
        String type,
        String url

) {
    public static ChatMessageServiceResponse createFrom(ChatMessage chatMessage, User sender) {
        return new ChatMessageServiceResponse(
                chatMessage.getId(),
                chatMessage.getChatRoom().getId(),
                sender.getId().getValue(),
                sender.getNickname().getValue(),
                sender.getProfileImage(),
                chatMessage.getContent().getValue(),
                chatMessage.getType().name(),
                chatMessage.getUrl()
        );
    }
}
