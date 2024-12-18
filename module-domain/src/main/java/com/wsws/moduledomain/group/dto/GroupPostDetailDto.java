package com.wsws.moduledomain.group.dto;

import java.time.LocalDateTime;

public record GroupPostDetailDto(
        Long groupPostId,
        String nickname,
        String profile,
        String content,
        LocalDateTime createAt,
        String userId,
        Long likeCount
) {
}
