package com.wsws.moduledomain.follow;


import com.wsws.moduledomain.follow.vo.FollowId;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Follow {
    private final FollowId id;
    private final LocalDateTime createdAt;

    private Follow(FollowId id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public static Follow create(String followerId, String followeeId) {
        return new Follow(FollowId.of(followerId, followeeId), LocalDateTime.now());
    }

    public static Follow of(FollowId id, LocalDateTime createdAt) {
        return new Follow(id, createdAt);
    }
}
