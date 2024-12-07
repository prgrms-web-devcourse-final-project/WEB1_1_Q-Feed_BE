package com.wsws.moduleinfra.entity.feed;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Answer")
@Getter
public class AnswerEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    Long id;

    String content;
    Boolean visibility;
    String url;
    int likeCount;
    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    QuestionEntity questionEntity;

    @OneToMany(mappedBy = "answerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<AnswerCommentEntity> answerCommentEntities = new ArrayList<>();

    @Column(nullable = false)
    String userId;

    public static AnswerEntity create(String content, Boolean visibility, String url, int likeCount, LocalDateTime createdAt, QuestionEntity questionEntity, String userId) {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.content = content;
        answerEntity.visibility = visibility;
        answerEntity.url = url;
        answerEntity.likeCount = likeCount;
        answerEntity.createdAt = createdAt;
        answerEntity.questionEntity = questionEntity;
        answerEntity.userId = userId;
        return answerEntity;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void setQuestionEntity(final QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }

    /**
     * 수정 로직
     */
    public void editQuestionEntity(String content, Boolean visibility, String url, int likeCount) {
        this.content = content;
        this.visibility = visibility;
        this.url = url;
        this.likeCount = likeCount;
    }

}