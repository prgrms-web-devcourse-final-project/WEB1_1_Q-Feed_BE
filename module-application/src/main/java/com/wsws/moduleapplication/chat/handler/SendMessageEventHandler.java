package com.wsws.moduleapplication.chat.handler;

import com.wsws.moduleapplication.chat.event.SendMessageEvent;
import com.wsws.moduleapplication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendMessageEventHandler {

    private final NotificationService notificationService;

    //채팅 알림 처리
    @EventListener
    public void handleSendMessageEvent(SendMessageEvent event) {
        notificationService.sendNotification(
                event.senderId(),
                event.recipientId(),
                null,
                null,
                null,
                null,
                event.fcmType()
        );
    }

}
