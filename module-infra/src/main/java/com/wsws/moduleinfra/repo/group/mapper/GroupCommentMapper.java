package com.wsws.moduleinfra.repo.group.mapper;

import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduleinfra.entity.group.GroupCommentEntity;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;

public class GroupCommentMapper {

    // **엔티티 -> 도메인**
    public static GroupComment toDomain(GroupCommentEntity entity) {
        return GroupComment.create(
                entity.getGroupCommentId(),
                entity.getContent(),
                entity.getCreatedAt(),
                String.valueOf(entity.getGroupCommentId()),
                entity.getLikeCount()
        );
    }

    // **도메인 -> 엔티티**
    public static GroupCommentEntity toEntity(GroupComment domain) {
        GroupCommentEntity entity = GroupCommentEntity.create(
                domain.getContent(),
                domain.getCreatedAt(),
                String.valueOf(domain.getUserId()),
                domain.getLikeCount(),
                domain.getGroupPostId()
//                groupPostEntity // 연관된 GroupPostEntity 설정
        );

        return entity;
    }
}
