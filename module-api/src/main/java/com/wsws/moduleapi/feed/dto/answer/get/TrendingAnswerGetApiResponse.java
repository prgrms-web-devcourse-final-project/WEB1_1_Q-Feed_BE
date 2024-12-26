package com.wsws.moduleapi.feed.dto.answer.get;

import com.wsws.moduleapplication.feed.dto.answer.read.TrendingAnswerFindServiceResponse;

public record TrendingAnswerGetApiResponse(
        Long answerId,
        String content
) {
    public static TrendingAnswerGetApiResponse toApiResponse(TrendingAnswerFindServiceResponse serviceResponse) {
        return new TrendingAnswerGetApiResponse(serviceResponse.answerId(), serviceResponse.content());
    }
}
