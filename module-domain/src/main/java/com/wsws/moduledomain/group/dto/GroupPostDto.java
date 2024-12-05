package com.wsws.moduledomain.group.dto;

import java.time.LocalDateTime;

public record GroupPostDto(
        Long groupPostId,
        String nickname,
        String profile,
        String content,
        LocalDateTime createAt,
        String userId
//        boolean hasReaction,
//        Long reactionCount,
//        Long commentCount
) {
}
