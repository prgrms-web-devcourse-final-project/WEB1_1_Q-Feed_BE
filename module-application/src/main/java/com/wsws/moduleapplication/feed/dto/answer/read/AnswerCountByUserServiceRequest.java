package com.wsws.moduleapplication.feed.dto.answer.read;

public record AnswerCountByUserServiceRequest(
        String reqUserId,
        String targetUserId
) {
}
