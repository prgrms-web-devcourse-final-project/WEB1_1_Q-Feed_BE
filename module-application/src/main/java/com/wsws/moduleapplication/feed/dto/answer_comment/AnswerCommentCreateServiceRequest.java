package com.wsws.moduleapplication.feed.dto.answer_comment;

public record AnswerCommentCreateServiceRequest(
        String userId,
        Long answerId,
        Long parentCommentId,
        String content
) {
}
