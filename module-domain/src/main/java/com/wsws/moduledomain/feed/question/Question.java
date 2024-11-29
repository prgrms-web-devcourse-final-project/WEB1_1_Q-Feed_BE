package com.wsws.moduledomain.feed.question;

import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    Long id;

    private String content;
    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;
    private LocalDateTime createdAt;

    private Long categoryId;


    public static Question create(String content, QuestionStatus questionStatus, LocalDateTime createdAt, Long categoryId) {
        Question question = new Question();
        question.content = content;
        question.questionStatus = questionStatus;
        question.createdAt = createdAt;
        question.categoryId = categoryId;
        return question;
    }

    // 비즈니스 로직 //

    /**
     * 오늘의 질문들 활성화
     */
    public void activateQuestion(){
        this.questionStatus = QuestionStatus.ACTIVATED;
    }

    /**
     * 하루가 지난 질문 비활성화
     */
    public void inactivateQuestion() {
        this.questionStatus = QuestionStatus.INACTIVATED;
    }

}
