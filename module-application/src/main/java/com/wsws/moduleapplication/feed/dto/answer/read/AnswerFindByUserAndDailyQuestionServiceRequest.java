package com.wsws.moduleapplication.feed.dto.answer.read;

public record AnswerFindByUserAndDailyQuestionServiceRequest(
        String reqUserId,
        Long questionId
) {
}
