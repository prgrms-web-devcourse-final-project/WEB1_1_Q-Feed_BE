package com.wsws.moduleapplication.dto.notification;

import com.wsws.moduledomain.notification.NotificationType;
import com.wsws.moduledomain.user.vo.UserId;

public record NotificationServiceRequestDto(
        UserId recipientId,
        NotificationType type,
        String content,
        UserId senderId
        //fcm 토큰
) {

}
