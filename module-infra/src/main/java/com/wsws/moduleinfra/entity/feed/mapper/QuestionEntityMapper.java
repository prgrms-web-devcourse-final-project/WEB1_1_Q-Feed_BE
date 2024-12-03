package com.wsws.moduleinfra.entity.feed.mapper;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;

public class QuestionEntityMapper {

    public static Question toDomain(QuestionEntity questionEntity) {
        return Question.create(
                questionEntity.getId(),
                questionEntity.getContent(),
                questionEntity.getQuestionStatus(),
                questionEntity.getCreatedAt(),
                questionEntity.getCategoryId()
        );
    }

    public static QuestionEntity toEntity(Question question) {
        return QuestionEntity.create(
                question.getContent(),
                question.getQuestionStatus(),
                question.getCreatedAt(),
                question.getCategoryId()
        );
    }
}
