package com.wsws.moduleinfra.entity.feed;

import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Question")
public class QuestionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    Long id;

    private String content;
    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long categoryId;


    public static QuestionEntity create(String content, QuestionStatus questionStatus, LocalDateTime createdAt, Long categoryId) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.content = content;
        questionEntity.questionStatus = questionStatus;
        questionEntity.createdAt = createdAt;
        questionEntity.categoryId = categoryId;
        return questionEntity;
    }

    /* 수정 로직*/
    public void editQuestionEntity(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;
    }


}
