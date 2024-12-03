package com.wsws.moduleapplication.feed.dto.answer;

public record AnswerLikeServiceRequest(
        String userId,
        Long answerId
) {
}
