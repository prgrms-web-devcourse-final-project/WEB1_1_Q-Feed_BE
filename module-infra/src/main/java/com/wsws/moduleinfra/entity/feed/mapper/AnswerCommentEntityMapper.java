package com.wsws.moduleinfra.entity.feed.mapper;

import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduleinfra.entity.feed.AnswerCommentEntity;

public class AnswerCommentEntityMapper {
    public static AnswerComment toDomain(AnswerCommentEntity answerCommentEntity) {
        Long answerId = answerCommentEntity.getAnswerEntity() != null
                ? answerCommentEntity.getAnswerEntity().getId()
                : null;

        Long parentCommentId = answerCommentEntity.getParentCommentEntity() != null
                ? answerCommentEntity.getParentCommentEntity().getId()
                : null;

        return AnswerComment.create(
                answerCommentEntity.getId(),
                answerCommentEntity.getContent(),
                answerCommentEntity.getDepth(),
                answerCommentEntity.getLikeCount(),
                answerId,
                answerCommentEntity.getUserId(),
                parentCommentId
        );
    }

    public static AnswerCommentEntity toEntity(AnswerComment answerComment) {
        return AnswerCommentEntity.create(
                answerComment.getContent(),
                answerComment.getDepth(),
                answerComment.getLikeCount(),
                null,
                answerComment.getUserId().getValue(),
                null
        );
    }
}
