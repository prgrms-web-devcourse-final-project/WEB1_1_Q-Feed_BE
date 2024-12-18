package com.wsws.moduleapi.feed.dto.question;


import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;

import java.time.LocalDate;

public record QuestionApiResponse(
        Long questionId,
        String content,
        LocalDate questionDate
) {
    public QuestionApiResponse(QuestionFindServiceResponse questionFindServiceResponse) {
        this(
                questionFindServiceResponse.questionId(),
                questionFindServiceResponse.content(),
                questionFindServiceResponse.questionDate()
        );
    }

}