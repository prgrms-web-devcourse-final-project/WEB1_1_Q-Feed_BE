package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduledomain.chat.dto.ChatMessageDTO;

import java.time.LocalDateTime;

public record ChatMessageServiceResponse(
        Long messageId,
        String content,
        String type,
        String url,
        Boolean isRead,
        LocalDateTime createdAt,
        String userId,
        String userNickName,
        String userProfileImage
) {
    public ChatMessageServiceResponse(ChatMessageDTO dto) {
        this(
                dto.messageId(),
                dto.content(),
                dto.type().toString(),
                dto.url(),
                dto.isRead(),
                dto.createdAt(),
                dto.userId(),
                dto.userNickName(),
                dto.userProfileImage()
        );
    }
}
