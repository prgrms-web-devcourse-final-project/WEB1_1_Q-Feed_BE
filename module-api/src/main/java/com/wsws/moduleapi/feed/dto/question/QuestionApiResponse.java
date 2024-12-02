package com.wsws.moduleapi.feed.dto.question;

import com.wsws.moduleapplication.feed.dto.QuestionServiceResponse;

public record QuestionApiResponse(
        Long questionId,
        String content
) {
    public QuestionApiResponse(QuestionServiceResponse questionServiceResponse) {
        this(
                questionServiceResponse.questionId(),
                questionServiceResponse.content()
        );
    }

}
