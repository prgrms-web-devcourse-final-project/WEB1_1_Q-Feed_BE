package com.wsws.moduleapi.feed.dto.answer.get;

import com.wsws.moduleapplication.feed.dto.answer.read.AnswerCountByUserServiceResponse;

public record AnswerCountByUserGetApiResponse(
        Long answerCount
) {
    public static AnswerCountByUserGetApiResponse toApiResponse(AnswerCountByUserServiceResponse serviceResponse) {
        return new AnswerCountByUserGetApiResponse(serviceResponse.answerCount());
    }
}

