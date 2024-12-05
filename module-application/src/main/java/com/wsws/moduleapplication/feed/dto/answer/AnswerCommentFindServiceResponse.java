package com.wsws.moduleapplication.feed.dto.answer;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AnswerCommentFindServiceResponse(
        Long commentId,
        String userId,
        String authorNickname,
        String profile_image,
        String content,
        int likeCount,
        LocalDateTime createdAt,
        Boolean isLike,
        Boolean isFollow,
        int childCommentCount,
        List<AnswerCommentFindServiceResponse> children
) {

}
