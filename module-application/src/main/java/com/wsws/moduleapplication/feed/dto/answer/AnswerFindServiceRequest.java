package com.wsws.moduleapplication.feed.dto.answer;

import java.time.LocalDateTime;

public record AnswerFindServiceRequest(
        String userId,
        Long answerId,
        LocalDateTime cursor,
        int size
) {
}
