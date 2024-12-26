package com.wsws.moduleapplication.feed.dto.answer.read;

public record TrendingAnswerFindServiceRequest(
        Long categoryId,
        int limit
) {
}
