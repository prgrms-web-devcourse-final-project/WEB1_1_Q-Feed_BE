package com.wsws.moduleapplication.feed.dto.answer;

import java.time.LocalDateTime;

public record AnswerCountByUserServiceRequest(
        String reqUserId,
        String targetUserId
) {
}
