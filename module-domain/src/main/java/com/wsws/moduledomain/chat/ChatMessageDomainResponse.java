package com.wsws.moduledomain.chat;

import com.wsws.moduledomain.user.User;

import java.time.LocalDateTime;

public record ChatMessageDomainResponse(
        Long messageId,
        Long chatRoomId,
        String senderId,
        String senderName,
        String senderProfileImage,
        String content,
        String type,
        String url
        //LocalDateTime createdAt

) {
    public static ChatMessageDomainResponse createFrom(ChatMessage chatMessage, User sender) {
        return new ChatMessageDomainResponse(
                chatMessage.getId(),
                chatMessage.getChatRoomId(),
                sender.getId().getValue(),
                sender.getNickname().getValue(),
                sender.getProfileImage(),
                chatMessage.getContent().getValue(),
                chatMessage.getType().name(),
                chatMessage.getUrl()
                //chatMessage.getCreatedAt()
        );
    }
}