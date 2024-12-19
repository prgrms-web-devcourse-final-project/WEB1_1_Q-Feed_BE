package com.wsws.moduleinfra.repo.group.mapper;

import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduleinfra.entity.group.GroupCommentEntity;

public class GroupCommentMapper {

    // 엔티티 -> 도메인
    public static GroupComment toDomain(GroupCommentEntity entity) {
        return GroupComment.create(
                entity.getGroupCommentId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUserId(),
                entity.getLikeCount(),
                entity.getGroupPost().getGroupPostId()
        );
    }

    // 도메인 -> 엔티티
    public static GroupCommentEntity toEntity(GroupComment domain) {
        if (domain == null) return null;
        return GroupCommentEntity.create(
                domain.getContent(),
                domain.getCreatedAt(),
                domain.getUserId().getValue(),
                domain.getLikeCount(),
                null
        );
    }
}
