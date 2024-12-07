package com.wsws.moduleapi.feed.dto.answer;

import java.time.LocalDateTime;

public record AnswerByUserGetApiResponse(
    Long answerId,
    LocalDateTime createdAt,
    String questionContent,
    boolean visibility
) {
}
