package com.wsws.moduleapi.feed.dto.answer_comment;

import jakarta.validation.constraints.NotNull;

public record AnswerCommentPostApiRequest(
        @NotNull(message = "Answer Id는 필수입니다.")
        Long answerId,
        Long parentCommentId,
        String content
) {
}
