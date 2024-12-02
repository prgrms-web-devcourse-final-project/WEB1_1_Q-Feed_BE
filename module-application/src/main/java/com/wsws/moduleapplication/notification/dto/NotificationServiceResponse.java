package com.wsws.moduleapplication.notification.dto;

import com.wsws.moduledomain.notification.Notification;

public record NotificationServiceResponse(
        Long id,
        String type,
        String sender,
        String recipient,
        boolean isRead
) {

    public NotificationServiceResponse(Notification notification) {
        this(
                notification.getId(),
                notification.getType(),
                notification.getSender().getValue(),
                notification.getRecipient().getValue(),
                notification.isRead()
        );
    }
}
