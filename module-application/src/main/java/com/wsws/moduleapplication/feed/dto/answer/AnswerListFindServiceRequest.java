package com.wsws.moduleapplication.feed.dto.answer;

import java.time.LocalDateTime;

public record AnswerListFindServiceRequest (
    String userId,
    LocalDateTime answerCursor,
    int size
) {
}
