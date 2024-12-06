package com.wsws.moduleinfra.entity.feed.mapper;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;

public class AnswerEntityMapper {
    public static Answer toDomain(AnswerEntity answerEntity) {

        Long questionId = answerEntity.getQuestionEntity() != null
                ? answerEntity.getQuestionEntity().getId()
                : null;


        return Answer.create(
                answerEntity.getId(),
                answerEntity.getContent(),
                answerEntity.getVisibility(),
                answerEntity.getUrl(),
                answerEntity.getLikeCount(),
                answerEntity.getCreatedAt(),
                questionId,
                answerEntity.getUserId()
        );
    }

    public static AnswerEntity toEntity(Answer answer) {
        return AnswerEntity.create(
                answer.getContent(),
                answer.getVisibility(),
                answer.getUrl(),
                answer.getLikeCount(),
                answer.getCreatedAt(),
                null,
                answer.getUserId().getValue()
        );
    }
}