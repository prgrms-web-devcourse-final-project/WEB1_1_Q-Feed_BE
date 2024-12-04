package com.wsws.moduleapplication.group.dto;

import java.time.LocalDateTime;

public record GroupPostDetailResponse(
        String authorNickName,
        String authorProfile,
        String content,
        LocalDateTime createAt,
        boolean hasReaction,
        boolean isFollowing,
        Integer reactionCount,
        Integer commentCount
//        List<CommentDetailResponse> comments
) {
}
