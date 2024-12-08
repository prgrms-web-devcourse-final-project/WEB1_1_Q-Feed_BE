package com.wsws.moduleapplication.group.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GroupPostDetailResponse(
        Long groupPostId,
        String userId,
        String content,
        LocalDateTime createdAt,
        Long likeCount,
        List<CommentResponse> comments
) {
    public record CommentResponse(
            Long commentId,
            String userId,
            String content,
            LocalDateTime createdAt,
            Long likeCount
    ) {}
}
