package com.wsws.moduleapplication.feed.dto.answer_comment;

public record AnswerCommentEditServiceRequest(
        Long answerCommentId,
        String content
) {
}
