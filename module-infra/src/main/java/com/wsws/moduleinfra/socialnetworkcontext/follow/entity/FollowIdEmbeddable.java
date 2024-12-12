package com.wsws.moduleinfra.socialnetworkcontext.follow.entity;

import com.wsws.moduledomain.socialnetwork.follow.vo.FollowId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FollowIdEmbeddable {

    @Column(name = "follower_id", nullable = false)
    private String followerId;

    @Column(name = "followee_id", nullable = false)
    private String followeeId;

    public FollowIdEmbeddable(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public static FollowIdEmbeddable fromDomain(FollowId followId) {
        return new FollowIdEmbeddable(followId.getFollowerId(), followId.getFolloweeId());
    }

    public static FollowId toDomain(FollowIdEmbeddable embeddable) {
        return FollowId.of(embeddable.getFollowerId(), embeddable.getFolloweeId());
    }
}