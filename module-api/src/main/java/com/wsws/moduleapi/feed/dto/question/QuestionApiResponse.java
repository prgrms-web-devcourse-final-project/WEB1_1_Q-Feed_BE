package com.wsws.moduleapi.feed.dto.question;


import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;

public record QuestionApiResponse(
        Long questionId,
        String content
) {
    public QuestionApiResponse(QuestionFindServiceResponse questionFindServiceResponse) {
        this(
                questionFindServiceResponse.questionId(),
                questionFindServiceResponse.content()
        );
    }

}