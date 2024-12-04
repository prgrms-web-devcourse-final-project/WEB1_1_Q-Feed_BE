package com.wsws.moduleinfra.repo.group.mapper;

import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;

public class GroupPostMapper {

    // Entity -> Domain 변환
    public static GroupPost toDomain(GroupPostEntity entity) {
        if (entity == null) {
            return null;
        }
        return GroupPost.create(
                entity.getContent(),
                entity.getGroupId(),
                entity.getUrl()
        );
    }

    // Domain -> Entity 변환
    public static GroupPostEntity toEntity(GroupPost domain) {
        if (domain == null) {
            return null;
        }
        return GroupPostEntity.create(
                domain.getContent(),
                domain.getGroupId(),
                domain.getUserId(),
                domain.getUrl()
        );
    }
}

