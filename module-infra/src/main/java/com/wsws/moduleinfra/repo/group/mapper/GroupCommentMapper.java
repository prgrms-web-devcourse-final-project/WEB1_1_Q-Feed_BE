package com.wsws.moduleinfra.repo.group.mapper;


import com.wsws.moduleapplication.group.dto.GroupPostDetailResponse;
import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduleinfra.entity.group.GroupCommentEntity;
import com.wsws.moduleapplication.group.dto.GroupPostDetailResponse.CommentResponse;


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

    public static GroupPostDetailResponse.CommentResponse toCommentResponse(GroupComment comment) {
        return new GroupPostDetailResponse.CommentResponse(
                comment.getGroupCommentId(),
                String.valueOf(comment.getUserId()),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getLikeCount()
        );
    }
}
