package com.wsws.moduleapi.feed.dto.answer_comment;

import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentEditServiceRequest;

public record AnswerCommentPutApiRequest(
        String content
) {
    public AnswerCommentEditServiceRequest toServiceDto(Long answerCommentId, String userId) {
        return new AnswerCommentEditServiceRequest(userId, answerCommentId, content);
    }
}
