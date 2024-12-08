package com.wsws.moduleapplication.group.dto;

import java.time.LocalDateTime;

public record CommentResponse (
        Long commentId,
        String authorNickname,
        String authorProfile,
        String content,
        LocalDateTime createdAt,
        int likeCount,
        String userId
)
{

}


