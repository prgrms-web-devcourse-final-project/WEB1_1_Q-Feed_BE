package com.wsws.moduleapi.feed.dto.question;


import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;

import java.time.LocalDateTime;

public record QuestionApiResponse(
        Long questionId,
        String content,
        LocalDateTime createdAt
) {
    public QuestionApiResponse(QuestionFindServiceResponse questionFindServiceResponse) {
        this(
                questionFindServiceResponse.questionId(),
                questionFindServiceResponse.content(),
                questionFindServiceResponse.createdAt()
        );
    }

}