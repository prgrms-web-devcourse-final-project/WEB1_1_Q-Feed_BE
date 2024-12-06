package com.wsws.moduleapplication.feed.dto.answer;


import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AnswerFindServiceResponse(
        Long answerId,
        String authorUserId,
        String authorNickname,
        String profileImage,
        String content,
        LocalDateTime createdAt,
        int likeCount,
        Boolean isLike,
        Boolean isFollowing,
        int commentCount,
        List<AnswerCommentFindServiceResponse> comments
) {
}
