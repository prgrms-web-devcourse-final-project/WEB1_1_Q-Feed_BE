package com.wsws.moduleapplication.group.handler;

import com.wsws.moduleapplication.group.event.GroupCommentCreatedEvent;
import com.wsws.moduleapplication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupCommentCreatedEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void handleGroupCommentCreatedEvent(GroupCommentCreatedEvent event) {

        //URL 생성
        String url = "/groups/posts/" + event.postId() + "#comment-" + event.commentId();

        notificationService.sendNotification(
                event.senderId(),
                event.recipientId(),
                event.postId(),
                event.commentId(),
                null,
                url,
                event.fcmType()
        );
    }
}
