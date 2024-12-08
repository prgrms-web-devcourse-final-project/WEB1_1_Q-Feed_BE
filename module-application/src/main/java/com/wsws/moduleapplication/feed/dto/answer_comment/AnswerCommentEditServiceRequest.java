package com.wsws.moduleapplication.feed.dto.answer_comment;

public record AnswerCommentEditServiceRequest(
        String userId,
        Long answerCommentId,
        String content
) {
}
