package com.wsws.moduleapplication.feed.handler;

import com.wsws.moduleapplication.feed.event.AnswerLikedEvent;
import com.wsws.moduleapplication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerLikedEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void handleAnswerLikedEvent(AnswerLikedEvent event) {

        String url = "/feed/answers/" + event.answerId();

        notificationService.sendNotification(
                event.likerId(),
                event.userId(),
                event.answerId(),
                null,
                null,
                url,
                event.fcmType());
    }


}
