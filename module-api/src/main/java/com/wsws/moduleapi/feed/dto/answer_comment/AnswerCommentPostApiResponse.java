package com.wsws.moduleapi.feed.dto.answer_comment;

import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceRequest;
import jakarta.validation.constraints.NotNull;

public record AnswerCommentPostApiResponse(
        Long answerCommentId,
        String message
) {
}
