package com.wsws.moduleapi.chat.dto;

import com.wsws.moduleapplication.chat.dto.ChatMessageServiceResponse;

import java.awt.*;
import java.time.LocalDateTime;

public record ChatMessageApiResponse(
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
    public ChatMessageApiResponse(ChatMessageServiceResponse serviceResponse) {
        this(
                serviceResponse.messageId(),
                serviceResponse.content(),
                serviceResponse.type(),
                serviceResponse.url(),
                serviceResponse.isRead(),
                serviceResponse.createdAt(),
                serviceResponse.userId(),
                serviceResponse.userNickName(),
                serviceResponse.userProfileImage()
        );
    }

}
