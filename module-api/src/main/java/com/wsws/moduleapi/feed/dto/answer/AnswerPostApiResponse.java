package com.wsws.moduleapi.feed.dto.answer;

import com.wsws.moduleapplication.feed.dto.AnswerCreateServiceResponse;

public record AnswerPostApiResponse(
        Long answerId,
        String message
) {
    public AnswerPostApiResponse(AnswerCreateServiceResponse response) {
        this(
                response.answerId(),
                "답변이 생성되었습니다."
        );
    }
}
