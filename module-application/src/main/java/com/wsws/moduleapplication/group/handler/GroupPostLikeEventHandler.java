package com.wsws.moduleapplication.group.handler;

import com.wsws.moduleapplication.group.event.GroupPostLikeEvent;
import com.wsws.moduleapplication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GroupPostLikeEventHandler {

    private final NotificationService notificationService;

    // 그룹 게시물 좋아요 알림 처리
    @EventListener
    public void handleGroupPostLikeEvent(GroupPostLikeEvent event) {
        notificationService.sendNotification(
                event.senderId(),
                event.recipientId(),
                event.postId(),
                null,
                null,
                "/groups/posts/" + event.postId(),
                event.fcmType()
        );
    }
}
