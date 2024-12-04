package com.wsws.moduleinfra.entity.feed;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AnswerCommentEntity {
    @Id @GeneratedValue
    private Long id;

    private String content;
    private int depth;
    private int reactionCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private AnswerEntity answerEntity;

    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private AnswerCommentEntity parentCommentEntity;

    public static AnswerCommentEntity create(String content, int depth, int reactionCount, AnswerEntity answerEntity, String userId, AnswerCommentEntity parentCommentEntity) {
        AnswerCommentEntity answerCommentEntity = new AnswerCommentEntity();
        answerCommentEntity.content = content;
        answerCommentEntity.depth = depth;
        answerCommentEntity.reactionCount = reactionCount;
        answerCommentEntity.answerEntity = answerEntity;
        answerCommentEntity.userId = userId;
        answerCommentEntity.parentCommentEntity = parentCommentEntity;

        return answerCommentEntity;
    }

    /* 연관관계 편의 메서드 */
    public void setAnswerEntity(AnswerEntity answerEntity) {
        this.answerEntity = answerEntity;
    }

    public void setParentCommentEntity(AnswerCommentEntity parentCommentEntity) {
        this.parentCommentEntity = parentCommentEntity;
    }

}
