package com.wsws.moduledomain.chat;

import com.wsws.moduledomain.user.User;

public record ChatMessageDomainResponse(
        Long messageId,
        Long chatRoomId,
        String senderId,
        String senderName,
        String senderProfileImage,
        String content,
        String type,
        String url

) {
    public static ChatMessageDomainResponse createFrom(ChatMessage chatMessage, User sender) {
        return new ChatMessageDomainResponse(
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