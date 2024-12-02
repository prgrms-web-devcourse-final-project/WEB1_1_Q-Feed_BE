package com.wsws.moduleinfra.entity.follow;


import com.wsws.moduledomain.follow.Follow;

public class FollowEntityMapper {

    // FollowEntity → Follow
    public static Follow toFollow(FollowEntity entity) {
        return Follow.of(
                FollowIdEmbeddable.toDomain(entity.getId()), // FollowIdEmbeddable → FollowId
                entity.getCreatedAt()
        );
    }

    // Follow → FollowEntity
    public static FollowEntity fromFollow(Follow follow) {
        return new FollowEntity(
                FollowIdEmbeddable.fromDomain(follow.getId()), // FollowId → FollowIdEmbeddable
                follow.getCreatedAt()
        );
    }
}
