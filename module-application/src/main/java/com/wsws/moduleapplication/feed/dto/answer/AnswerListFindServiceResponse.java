package com.wsws.moduleapplication.feed.dto.answer;

import java.time.LocalDateTime;
import java.util.List;


public record AnswerListFindServiceResponse(
        List<AnswerFindServiceResponse> answers
) {
}
