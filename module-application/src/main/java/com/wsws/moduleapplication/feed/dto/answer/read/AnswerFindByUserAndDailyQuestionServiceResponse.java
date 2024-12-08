package com.wsws.moduleapplication.feed.dto.answer.read;

import java.time.LocalDateTime;

public record AnswerFindByUserAndDailyQuestionServiceResponse(
        Long answerId,
        String answerContent,
        LocalDateTime createdAt
) {
}
