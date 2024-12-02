package com.wsws.moduleinfra.entity.feed;

import jakarta.persistence.*;
import lombok.Getter;

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
    int reactionCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    QuestionEntity questionEntity;

    String userId;

    public static AnswerEntity create(String content, Boolean visibility, String url, int reactionCount, QuestionEntity questionEntity, String userId) {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.content = content;
        answerEntity.visibility = visibility;
        answerEntity.url = url;
        answerEntity.reactionCount = reactionCount;
        answerEntity.questionEntity = questionEntity;
        answerEntity.userId = userId;
        return answerEntity;
    }
}
