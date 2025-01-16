package com.wsws.moduleapplication.feed.handler;


import com.wsws.moduleapplication.feed.event.AnswerCommentCreatedEvent;
import com.wsws.moduleapplication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerCommentCreatedEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void handleAnswerCommentCreatedEvent(AnswerCommentCreatedEvent event) {

        String url = "/post/" + event.answerId() + "#comment-" + event.commentId();

        notificationService.sendNotification(
                event.senderId(),
                event.recipientId(),
                event.answerId(),
                event.commentId(),
                null,
                url,
                event.fcmType());
    }

}
