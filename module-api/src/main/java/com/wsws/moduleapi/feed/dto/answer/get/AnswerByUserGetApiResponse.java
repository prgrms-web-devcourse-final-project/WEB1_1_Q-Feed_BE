package com.wsws.moduleapi.feed.dto.answer.get;

import com.wsws.moduleapplication.feed.dto.answer.read.AnswerFindByUserServiceResponse;

import java.time.LocalDateTime;

public record AnswerByUserGetApiResponse(
        Long answerId,
        LocalDateTime createdAt,
        String answerContent,
        String questionContent,
        boolean visibility
) {
    public static AnswerByUserGetApiResponse toApiResponse(AnswerFindByUserServiceResponse serviceResponse) {
        return new AnswerByUserGetApiResponse(
                serviceResponse.answerId(),
                serviceResponse.createdAt(),
                serviceResponse.answerContent(),
                serviceResponse.questionContent(),
                serviceResponse.visibility()
        );
    }
}
