package com.wsws.moduleapplication.feed.handler;


import com.wsws.moduleapplication.feed.event.AnswerCommentLikedEvent;
import com.wsws.moduleapplication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerCommentLikedEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void handleAnswerCommentLikedEvent(AnswerCommentLikedEvent event) {
        String url = "/post/" + event.answerId() + "#comment-" + event.answerCommentId();
        notificationService.sendNotification(
                event.senderId(),
                event.recipientId(),
                event.answerId(),
                event.answerCommentId(),
                null,
                url,
                event.fcmType());
    }
}
