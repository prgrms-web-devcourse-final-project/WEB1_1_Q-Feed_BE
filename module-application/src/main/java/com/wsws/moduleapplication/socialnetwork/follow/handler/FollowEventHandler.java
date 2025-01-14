package com.wsws.moduleapplication.socialnetwork.follow.handler;

import com.wsws.moduleapplication.notification.service.NotificationService;
import com.wsws.moduleapplication.socialnetwork.follow.event.FollowCreatedEvent;
import com.wsws.moduleapplication.socialnetwork.follow.event.FollowDeletedEvent;
import com.wsws.moduleapplication.socialnetwork.follow.event.FollowEvent;
import com.wsws.moduledomain.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowEventHandler {

    private final NotificationService notificationService;
    private final CacheManager cacheManager;

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

    @EventListener
    public void handleFollowCreated(FollowCreatedEvent event) {
        cacheManager.evict("user:" + event.followerId() + ":followingCount");
        cacheManager.evict("user:" + event.followeeId() + ":followerCount");
    }

    @EventListener
    public void handleFollowDeleted(FollowDeletedEvent event) {
        cacheManager.evict("user:" + event.followerId() + ":followingCount");
        cacheManager.evict("user:" + event.followeeId() + ":followerCount");
    }
}
