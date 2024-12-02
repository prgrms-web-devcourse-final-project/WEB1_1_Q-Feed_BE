package com.wsws.moduledomain.chat.dto;

import com.wsws.moduledomain.chat.MessageType;

import java.time.LocalDateTime;

public record ChatMessageDTO(
        Long messageId,
        String content,
        MessageType type,
        String url,
        Boolean isRead,
        LocalDateTime createdAt,
        String userId,
        String userNickName,
        String userProfileImage
) {
}