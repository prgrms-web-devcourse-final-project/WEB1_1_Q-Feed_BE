package com.wsws.moduledomain.feed.question;

import com.wsws.moduledomain.feed.question.vo.QuestionId;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {
    private QuestionId questionId;
    private String content;
    private QuestionStatus questionStatus;
    private LocalDateTime createdAt;
    private Long categoryId;


    public static Question create(Long questionId, String content, QuestionStatus questionStatus, LocalDateTime createdAt, Long categoryId) {
        Question question = new Question();
        question.questionId = QuestionId.of(questionId);
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
