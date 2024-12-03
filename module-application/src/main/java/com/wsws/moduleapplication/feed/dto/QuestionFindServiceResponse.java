package com.wsws.moduleapplication.feed.dto;

import com.wsws.moduledomain.feed.question.Question;

public record QuestionFindServiceResponse(
        Long questionId,
        String content
) {

    public QuestionFindServiceResponse(Question question) {
        this(
                question.getQuestionId().getValue(),
                question.getContent()
        );
    }
}
