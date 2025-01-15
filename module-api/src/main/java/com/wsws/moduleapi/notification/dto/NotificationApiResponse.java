package com.wsws.moduleapi.notification.dto;

import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;

import java.time.LocalDateTime;

public record NotificationApiResponse(
      Long notificationId,
      LocalDateTime createdAt,
      String type,
      String content,
      String sender,
      String recipient,
      boolean isRead,
      String url,
      String profileImage
      ) {
    public NotificationApiResponse(NotificationServiceResponse serviceResponse) {
        this(
                serviceResponse.notificationId(),
                serviceResponse.createdAt(),
                serviceResponse.type(),
                serviceResponse.content(),
                serviceResponse.sender(),
                serviceResponse.recipient(),
                serviceResponse.isRead(),
                serviceResponse.url(),
                serviceResponse.profileImage()
        );

    }
}
