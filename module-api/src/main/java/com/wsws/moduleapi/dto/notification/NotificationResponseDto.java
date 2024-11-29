package com.wsws.moduleapi.dto.notification;

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
