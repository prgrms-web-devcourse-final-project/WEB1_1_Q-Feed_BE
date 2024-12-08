package com.wsws.moduleapplication.feed.dto.question;

import com.wsws.moduledomain.feed.question.Question;

import java.time.LocalDateTime;

public record QuestionFindServiceResponse(
        Long questionId,
        String content,
        LocalDateTime createdAt
) {

    public QuestionFindServiceResponse(Question question) {
        this(
                question.getQuestionId().getValue(),
                question.getContent(),
                question.getCreatedAt()
        );
    }
}
