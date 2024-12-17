package com.wsws.moduleapplication.group.dto;

import com.wsws.moduledomain.group.dto.GroupCommentDto;

import java.time.LocalDateTime;

public record GroupCommentServiceResponse(
        Long  groupCommentId,
        String content,
        LocalDateTime createdAt,
        String userId,
        String nickName,
        Long likeCount,
        Long groupPostId
) {
    public GroupCommentServiceResponse (GroupCommentDto groupCommentDto) {
        this (
                groupCommentDto.groupCommentId(),
                groupCommentDto.content(),
                groupCommentDto.createdAt(),
                groupCommentDto.userId(),
                groupCommentDto.nickname(),
                groupCommentDto.likeCount(),
                groupCommentDto.groupPostId()
        );

    }
}
