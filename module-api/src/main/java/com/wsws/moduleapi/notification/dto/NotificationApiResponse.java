package com.wsws.moduleapi.notification.dto;

import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;

public record NotificationApiResponse(
      Long id,
      String type,
      String sender,
      String recipient,
      boolean isRead
) {
    public NotificationApiResponse(NotificationServiceResponse serviceResponse) {
        this(
                serviceResponse.id(),
                serviceResponse.type(),
                serviceResponse.sender(),
                serviceResponse.recipient(),
                serviceResponse.isRead()
        );

    }
}
