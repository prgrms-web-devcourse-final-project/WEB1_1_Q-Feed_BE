package com.wsws.moduleapplication.group.handler;


import com.wsws.moduleapplication.group.event.GroupCommentLikedEvent;
import com.wsws.moduleapplication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupCommentLikedEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void handleGroupCommentLikedEvent(GroupCommentLikedEvent event) {

        String url = "/qspace/details/" + event.postId() + "#comment-" + event.commentId();

        notificationService.sendNotification(
                event.senderId(),
                event.recipientId(),
                null,
                event.commentId(),
                null,
                url,
                event.fcmType()
        );
    }
}
