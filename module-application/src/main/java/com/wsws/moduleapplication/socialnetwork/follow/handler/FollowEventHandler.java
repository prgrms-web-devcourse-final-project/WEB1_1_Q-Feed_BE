package com.wsws.moduleapplication.socialnetwork.follow.handler;

import com.wsws.moduleapplication.notification.service.NotificationService;
import com.wsws.moduleapplication.socialnetwork.follow.event.FollowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void handleFollowEvent(FollowEvent event) {
        notificationService.sendNotification(
                event.followerId(),
                event.followeeId(),
                null,
                null,
                null,
                "/profile/users/" + event.followerId(),
                event.fcmType()
        );
    }
}
