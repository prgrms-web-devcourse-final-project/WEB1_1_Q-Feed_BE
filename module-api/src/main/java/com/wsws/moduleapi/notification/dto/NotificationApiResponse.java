package com.wsws.moduleapi.notification.dto;

import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;

public record NotificationApiResponse(
      Long notificationId,
      String type,
      String content,
      String sender,
      String recipient,
      boolean isRead,
      String url
) {
    public NotificationApiResponse(NotificationServiceResponse serviceResponse) {
        this(
                serviceResponse.notificationId(),
                serviceResponse.type(),
                serviceResponse.content(),
                serviceResponse.sender(),
                serviceResponse.recipient(),
                serviceResponse.isRead(),
                serviceResponse.url()
        );

    }
}
