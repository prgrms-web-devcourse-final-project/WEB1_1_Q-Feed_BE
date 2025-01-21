package com.wsws.moduleapplication.feed.dto.answer.read;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
public record AnswerCommentFindServiceResponse(
        Long commentId,
        String userId,
        String authorNickname,
        String profileImage,
        String content,
        int likeCount,
        LocalDateTime createdAt,
        boolean isLike,
        boolean isFollowing,
        AtomicInteger childCommentCount,
        Long parentCommentId,
        List<AnswerCommentFindServiceResponse> children
) {
    public void changeChildCommentCount(int value) {
        this.childCommentCount.set(value);
    }
}
