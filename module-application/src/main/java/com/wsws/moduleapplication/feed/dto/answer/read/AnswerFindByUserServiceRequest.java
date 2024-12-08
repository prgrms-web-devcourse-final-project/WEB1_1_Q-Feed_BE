package com.wsws.moduleapplication.feed.dto.answer.read;

import java.time.LocalDateTime;

public record AnswerFindByUserServiceRequest(
        String reqUserId,
        String targetUserId,
        LocalDateTime cursor,
        int size
) {
}
