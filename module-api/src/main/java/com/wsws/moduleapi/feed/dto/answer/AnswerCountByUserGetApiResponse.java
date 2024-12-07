package com.wsws.moduleapi.feed.dto.answer;

import com.wsws.moduleapplication.feed.dto.answer.AnswerCountByUserServiceResponse;

public record AnswerCountByUserGetApiResponse(
        Long answerCount
) {
    public static AnswerCountByUserGetApiResponse toApiResponse(AnswerCountByUserServiceResponse serviceResponse) {
        return new AnswerCountByUserGetApiResponse(serviceResponse.answerCount());
    }
}

