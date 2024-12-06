package com.wsws.moduleapplication.feed.dto.answer;

public record AnswerFindServiceRequest(
        String userId,
        Long answerId
) {
}
