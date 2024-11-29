package com.wsws.moduleapi.notification.dto;

import com.wsws.moduledomain.notification.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponseDto (
        Long notificationId,
        NotificationType type,
        boolean isRead,
        String senderId,
        String recipientId,
        LocalDateTime createdAt
){
}
