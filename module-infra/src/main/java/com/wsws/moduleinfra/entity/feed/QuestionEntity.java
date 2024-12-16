package com.wsws.moduleinfra.entity.feed;

import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

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
    private LocalDate questionDate;

    @Column(nullable = false)
    private Long categoryId;


    public static QuestionEntity create(String content, QuestionStatus questionStatus, LocalDate questionDate, Long categoryId) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.content = content;
        questionEntity.questionStatus = questionStatus;
        questionEntity.questionDate = questionDate;
        questionEntity.categoryId = categoryId;
        return questionEntity;
    }

    /* 수정 로직*/
    public void editQuestionEntity(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;
    }


}
