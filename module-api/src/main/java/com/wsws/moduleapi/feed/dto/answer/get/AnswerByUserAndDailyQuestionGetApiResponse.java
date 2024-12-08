package com.wsws.moduleapi.feed.dto.answer.get;

import com.wsws.moduleapplication.feed.dto.answer.read.AnswerFindByUserAndDailyQuestionServiceResponse;

import java.time.LocalDateTime;

public record AnswerByUserAndDailyQuestionGetApiResponse(
        Long answerId,
        String answerContent,
        LocalDateTime createdAt
) {
    public static AnswerByUserAndDailyQuestionGetApiResponse toApiResponse(AnswerFindByUserAndDailyQuestionServiceResponse serviceResponse) {
        return new AnswerByUserAndDailyQuestionGetApiResponse(
                serviceResponse.answerId(),
                serviceResponse.answerContent(),
                serviceResponse.createdAt()
        );
    }
}
