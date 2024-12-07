package com.wsws.moduleinfra.entity.feed;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "answer_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AnswerCommentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_comment_id")
    private Long id;

    private String content;
    private int depth;
    private int likeCount;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private AnswerEntity answerEntity;

    @Column(nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private AnswerCommentEntity parentCommentEntity;

    @OneToMany(mappedBy = "parentCommentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerCommentEntity> childrenCommentEntity;

    public static AnswerCommentEntity create(String content, int depth, int likeCount, LocalDateTime createdAt, AnswerEntity answerEntity, String userId, AnswerCommentEntity parentCommentEntity) {
        AnswerCommentEntity answerCommentEntity = new AnswerCommentEntity();
        answerCommentEntity.content = content;
        answerCommentEntity.depth = depth;
        answerCommentEntity.likeCount = likeCount;
        answerCommentEntity.createdAt = createdAt;
        answerCommentEntity.answerEntity = answerEntity;
        answerCommentEntity.userId = userId;
        answerCommentEntity.parentCommentEntity = parentCommentEntity;

        return answerCommentEntity;
    }

    /* 연관관계 편의 메서드 */
    public void setAnswerEntity(AnswerEntity answerEntity) {
        this.answerEntity = answerEntity;
        answerEntity.getAnswerCommentEntities().add(this);
    }

    public void setParentCommentEntity(AnswerCommentEntity parentCommentEntity) {
        this.parentCommentEntity = parentCommentEntity;
    }


     /* 수정 로직 */
    public void editAnswerCommentEntity(String content, int likeCount) {
        this.content = content;
        this.likeCount = likeCount;
    }
}
