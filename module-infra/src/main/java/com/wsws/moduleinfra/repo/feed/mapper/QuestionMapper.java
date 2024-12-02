package com.wsws.moduleinfra.repo.feed.mapper;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.vo.QuestionId;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;

public class QuestionMapper {
    public static Question toDomain(QuestionEntity entity) {
        return Question.create(
                entity.getId(),
                entity.getContent(),
                entity.getQuestionStatus(),
                entity.getCreatedAt(),
                entity.getCategoryId()
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
