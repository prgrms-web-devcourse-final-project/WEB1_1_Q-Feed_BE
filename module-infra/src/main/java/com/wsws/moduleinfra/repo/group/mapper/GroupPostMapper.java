package com.wsws.moduleinfra.repo.group.mapper;

import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;

public class GroupPostMapper {

    // 엔티티 -> 도메인
    public static GroupPost toDomain(GroupPostEntity entity) {
        if (entity == null) {
            return null;
        }
        return GroupPost.create(
                entity.getGroupPostId(),
                entity.getGroupId(),
                entity.getContent(),
                entity.getUrl(),
                entity.getUserId(),
                entity.getLikeCount()
        );
    }

    // 도메인 -> 엔티티
    public static GroupPostEntity toEntity(GroupPost domain) {
        if (domain == null) {
            return null;
        }
        return GroupPostEntity.create(
                domain.getContent(),
                domain.getGroupId().getValue(),
                domain.getUserId().getValue(),
                domain.getUrl(),
                domain.getLikeCount()
        );
    }
}

