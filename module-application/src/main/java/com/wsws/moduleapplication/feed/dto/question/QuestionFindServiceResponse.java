package com.wsws.moduleapplication.feed.dto.question;

import com.wsws.moduledomain.feed.question.Question;
import java.time.LocalDate;

public record QuestionFindServiceResponse(
        Long questionId,
        String content,
        LocalDate questionDate
) {

    public QuestionFindServiceResponse(Question question) {
        this(
                question.getQuestionId().getValue(),
                question.getContent(),
                question.getQuestionDate()
        );
    }
}
