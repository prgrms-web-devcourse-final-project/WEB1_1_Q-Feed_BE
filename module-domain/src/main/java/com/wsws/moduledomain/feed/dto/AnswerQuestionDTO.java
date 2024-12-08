package com.wsws.moduledomain.feed.dto;

import java.time.LocalDateTime;

public record AnswerQuestionDTO (
        Long answerId,
        LocalDateTime createdAt,
        String questionContent,
        Boolean visibility
) {
}
