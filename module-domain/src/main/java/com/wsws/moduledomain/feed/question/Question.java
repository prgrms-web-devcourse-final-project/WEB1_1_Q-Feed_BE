package com.wsws.moduledomain.feed.question;

import com.wsws.moduledomain.feed.category.Category;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    public static Question create(String content, QuestionStatus questionStatus, LocalDateTime createdAt, Category category) {
        Question question = new Question();
        question.content = content;
        question.questionStatus = questionStatus;
        question.createdAt = createdAt;
        question.category = category;
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
