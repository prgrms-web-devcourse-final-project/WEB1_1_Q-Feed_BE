package com.wsws.moduleapplication.feed.dto.answer.read;

import java.time.LocalDateTime;

public record AnswerFindByUserServiceRequest(
        String userId,
        Long answerId,
        LocalDateTime cursor,
        int size
) {
}
