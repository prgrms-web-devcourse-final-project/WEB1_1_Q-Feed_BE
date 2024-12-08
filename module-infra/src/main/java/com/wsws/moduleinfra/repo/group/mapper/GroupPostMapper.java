package com.wsws.moduleinfra.repo.group.mapper;

import com.wsws.moduleapplication.group.dto.GroupPostDetailResponse;
import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;

import java.util.List;
import java.util.stream.Collectors;

public class GroupPostMapper {

    // Entity -> Domain 변환
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

    // Domain -> Entity 변환
    public static GroupPostEntity toEntity(GroupPost domain) {
        if (domain == null) {
            return null;
        }
        return GroupPostEntity.create(
                domain.getContent(),
                domain.getGroupId(),
                domain.getUserId(),
                domain.getUrl(),
                domain.getLikeCount()
        );
    }

    public static GroupPostDetailResponse toDetailResponse(GroupPost groupPost, List<GroupComment> comments) {
        List<GroupPostDetailResponse.CommentResponse> commentResponses = comments.stream()
                .map(GroupCommentMapper::toCommentResponse)
                .collect(Collectors.toList());

        return new GroupPostDetailResponse(
                groupPost.getGroupPostId(),
                groupPost.getUserId(),
                groupPost.getContent(),
                groupPost.getCreatedAt(),
                groupPost.getLikeCount(),
                commentResponses
        );
    }
}

