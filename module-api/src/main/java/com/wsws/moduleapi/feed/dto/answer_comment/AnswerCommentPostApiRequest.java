package com.wsws.moduleapi.feed.dto.answer_comment;

import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceRequest;
import jakarta.validation.constraints.NotNull;

public record AnswerCommentPostApiRequest(
        @NotNull(message = "Answer Id는 필수입니다.")
        Long answerId,
        Long parentCommentId,
        String content
) {
    public AnswerCommentCreateServiceRequest toServiceDto(String userId) {
        return new AnswerCommentCreateServiceRequest(userId, answerId, parentCommentId, content);
    }
}
