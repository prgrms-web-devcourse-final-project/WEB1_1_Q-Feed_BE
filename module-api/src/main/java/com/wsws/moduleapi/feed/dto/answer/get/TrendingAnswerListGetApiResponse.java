package com.wsws.moduleapi.feed.dto.answer.get;

import com.wsws.moduleapplication.feed.dto.answer.read.AnswerListFindServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.read.TrendingAnswerListFindServiceResponse;

import java.util.List;

public record TrendingAnswerListGetApiResponse(
        List<TrendingAnswerGetApiResponse> trendingAnswers
) {

    public static TrendingAnswerListGetApiResponse toApiResponse(TrendingAnswerListFindServiceResponse serviceResponse) {
        return new TrendingAnswerListGetApiResponse(serviceResponse.trendingAnswers().stream()
                .map(TrendingAnswerGetApiResponse::toApiResponse)
                .toList());
    }
}
