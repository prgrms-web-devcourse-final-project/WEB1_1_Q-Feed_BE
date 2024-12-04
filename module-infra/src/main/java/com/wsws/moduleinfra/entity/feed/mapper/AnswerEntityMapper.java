package com.wsws.moduleinfra.entity.feed.mapper;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;

public class AnswerEntityMapper {
    public static Answer toDomain(AnswerEntity answerEntity) {

        Long questionId = null;
        if (answerEntity.getQuestionEntity() != null) {
            questionId = answerEntity.getQuestionEntity().getId();
        }

        return Answer.create(
                answerEntity.getId(),
                answerEntity.getContent(),
                answerEntity.getVisibility(),
                answerEntity.getUrl(),
                answerEntity.getReactionCount(),
                questionId,
                answerEntity.getUserId()
        );
    }

    public static AnswerEntity toEntity(Answer answer) {
        return AnswerEntity.create(
                answer.getContent(),
                answer.getVisibility(),
                answer.getUrl(),
                answer.getReactionCount(),
                null,
                answer.getUserId().getValue()
        );
    }
}