package com.wsws.moduleapplication.feed.dto;

import com.wsws.moduledomain.feed.question.Question;

public record QuestionServiceResponse(
        Long questionId,
        String content
) {

    public QuestionServiceResponse(Question question) {
        this(
                question.getQuestionId().getValue(),
                question.getContent()
        );
    }
}
