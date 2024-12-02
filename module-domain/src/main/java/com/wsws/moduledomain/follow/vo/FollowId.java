package com.wsws.moduledomain.follow.vo;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class FollowId {
    private final String followerId;
    private final String followeeId;

    private FollowId(String followerId, String followeeId) {
        //팔로우 팔로워 있는지 검증 도메인 로직 추가 필요
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public static FollowId of(String followerId, String followeeId) {
        return new FollowId(followerId, followeeId);
    }
}
