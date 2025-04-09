package com.wsws.moduleapi.notification.dto;

public record NotificationTestRequest(
        String senderId,
        String recipientId,
        String type
) {}
