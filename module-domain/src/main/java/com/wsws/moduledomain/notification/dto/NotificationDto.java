package com.wsws.moduledomain.notification.dto;

public record NotificationDto(
        Long id,
        String type,
        String sender,
        String recipient,
        boolean isRead
) {
}
