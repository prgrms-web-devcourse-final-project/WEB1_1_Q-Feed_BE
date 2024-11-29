package com.wsws.moduleinfra.repo.notification.dto;

import com.wsws.moduledomain.notification.NotificationType;
import com.wsws.moduledomain.user.vo.UserId;

import java.time.LocalDateTime;

public record NotificationResponseInfraDto (
        Long notificationId,
        NotificationType type,
        boolean isRead,
        UserId senderId,
        UserId recipientId,
        LocalDateTime createdAt
) {
}
