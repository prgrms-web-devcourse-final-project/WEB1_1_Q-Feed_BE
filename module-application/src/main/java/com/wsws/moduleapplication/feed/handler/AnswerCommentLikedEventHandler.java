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
        String url = "/feed/answers/" + event.answerId() + "#comment-" + event.answerCommentId();
        notificationService.sendNotification(
                event.likerId(),
                event.userId(),
                event.answerId(),
                event.answerCommentId(),
                null,
                url,
                event.fcmType());
    }
}
