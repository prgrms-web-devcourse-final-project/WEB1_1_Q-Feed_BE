package com.wsws.moduleapplication.feed.dto.answer.read;

import java.util.List;

public record TrendingAnswerListFindServiceResponse(
        List<TrendingAnswerFindServiceResponse> trendingAnswers
) {
}
