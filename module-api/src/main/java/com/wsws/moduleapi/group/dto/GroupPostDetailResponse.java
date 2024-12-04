package com.wsws.moduleapi.group.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GroupPostDetailResponse(
        String authorNickName,
        String authorProfile,
        String content,
        LocalDateTime createAt,
//        boolean hasReaction,
        boolean isFollowing
//        Integer reactionCount,
//        Integer commentCount,
//        List<CommentDetailResponse> comments
) {
}
