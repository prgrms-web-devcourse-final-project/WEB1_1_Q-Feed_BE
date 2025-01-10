package com.wsws.moduleapplication.usercontext.user.handler;

import com.wsws.moduleapplication.usercontext.user.event.UserDeletedEvent;
import com.wsws.moduleapplication.usercontext.user.event.UserUpdatedEvent;
import com.wsws.moduledomain.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventHandler {

    private final CacheManager cacheManager;

    @EventListener
    public void handleUserUpdated(UserUpdatedEvent event){
        String profileCacheKey = "user:" + event.userId() + ":profile";
        cacheManager.evict(profileCacheKey);
    }

    @EventListener
    public void handleUserDeleted(UserDeletedEvent event){
        String profileCacheKey = "user:" + event.userId() + ":profile";
        cacheManager.evict(profileCacheKey);
    }
}
