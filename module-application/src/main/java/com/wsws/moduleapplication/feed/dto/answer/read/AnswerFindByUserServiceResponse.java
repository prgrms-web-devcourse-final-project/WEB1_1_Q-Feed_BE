package com.wsws.moduleapplication.feed.dto.answer.read;

import com.wsws.moduledomain.feed.dto.AnswerQuestionDTO;

import java.time.LocalDateTime;

public record AnswerFindByUserServiceResponse(
        Long answerId,
        LocalDateTime createdAt,
        String questionContent,
        Boolean visibility
) {
    public static AnswerFindByUserServiceResponse toServiceResponse(AnswerQuestionDTO dto) {
        return new AnswerFindByUserServiceResponse(
                dto.answerId(),
                dto.createdAt(),
                dto.questionContent(),
                dto.visibility());
    }
}