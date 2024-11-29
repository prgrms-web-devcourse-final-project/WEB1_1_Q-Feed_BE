package com.wsws.moduleapi.notification.dto;

import com.wsws.moduledomain.notification.NotificationType;
import com.wsws.moduledomain.user.vo.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequestDto(
        @NotBlank
        UserId recipientId,
        @NotNull
        NotificationType type,
        @NotBlank
        String content,
        @NotBlank
        UserId senderId
        //fcm 토큰
) {
}
