package com.wsws.moduleinfra.entity.user;

import com.wsws.moduledomain.user.Like;

public class LikeEntityMapper {

    public static Like toDomain(LikeEntity likeEntity) {

        String userId = null;
        if (likeEntity.getUserEntity() != null) {
            userId = likeEntity.getUserEntity().getId();
        }

        return Like.create(
                likeEntity.getId(),
                likeEntity.getTargetType(),
                likeEntity.getTargetId(),
                userId
        );
    }

    public static LikeEntity toEntity(Like like) {
        return LikeEntity.create(
                like.getTargetType(),
                like.getTargetId().getValue(),
                null // 연관관계는 연관관계 편의 메서드를 이용
        );
    }


}
