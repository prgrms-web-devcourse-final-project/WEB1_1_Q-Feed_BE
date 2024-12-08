package com.wsws.moduleapplication.feed.dto.answer.edit;

public record AnswerVisibilityEditServiceRequest(
        Long answerId,
        String reqUserId,
        Boolean visibility
) {
}
