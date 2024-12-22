package com.wsws.moduleapplication.notification.dto;

import com.wsws.moduledomain.notification.dto.NotificationDto;

public record NotificationServiceResponse(
        Long notificationId,
        String type,
        String content,
        String sender,
        String recipient,
        boolean isRead,
        String url
) {

    public NotificationServiceResponse(NotificationDto dto) {
        this(
                dto.notificationId(),
                dto.type(),
                dto.content(),
                dto.sender(),
                dto.recipient(),
                dto.isRead(),
                dto.url()
        );
    }
}
