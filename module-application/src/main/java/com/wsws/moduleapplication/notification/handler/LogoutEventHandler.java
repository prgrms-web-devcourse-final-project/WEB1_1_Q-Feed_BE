package com.wsws.moduleapplication.notification.handler;

import com.wsws.moduleapplication.notification.event.LogoutEvent;
import com.wsws.moduleapplication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void handleLogoutEvent(LogoutEvent event) {

        // 레디스에서 FCM 토큰 삭제
        notificationService.deleteFcmToken(event.userId());
    }


}
