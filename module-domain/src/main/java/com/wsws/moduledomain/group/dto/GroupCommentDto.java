package com.wsws.moduledomain.group.dto;


import java.time.LocalDateTime;

public record GroupCommentDto(
        Long  groupCommentId,
        String content,
        LocalDateTime createdAt,
        String userId,
        String nickname,
        Long likeCount,
        Long groupPostId
) {
}
