package com.wsws.moduleapplication.notification.dto;

import com.wsws.moduledomain.notification.NotificationType;
import com.wsws.moduledomain.user.vo.UserId;

import java.time.LocalDateTime;

public record NotificationServiceResponseDto(
        Long notificationId,
        NotificationType type,
        boolean isRead,
        UserId senderId,
        UserId recipientId,
        LocalDateTime createdAt
) {
}
