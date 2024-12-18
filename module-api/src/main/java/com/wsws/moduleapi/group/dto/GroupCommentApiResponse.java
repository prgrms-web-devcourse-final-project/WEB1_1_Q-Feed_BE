package com.wsws.moduleapi.group.dto;

import com.wsws.moduleapplication.group.dto.GroupCommentServiceResponse;

import java.time.LocalDateTime;

public record GroupCommentApiResponse(
        Long groupCommentId,
        String content,
        LocalDateTime createdAt,
        String userId,
        String nickname,
        long likeCount,
        Long groupPostId
) {
    // GroupCommentDto를 인자로 받는 생성자
    public GroupCommentApiResponse(GroupCommentServiceResponse dto) {
        this(
                dto.groupCommentId(),
                dto.content(),
                dto.createdAt(),
                dto.userId(),
                dto.nickName(),
                dto.likeCount(),
                dto.groupPostId()
        );
    }

}