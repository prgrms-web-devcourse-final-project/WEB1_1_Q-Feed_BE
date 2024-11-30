package com.wsws.moduledomain.feed.question.repo;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;

import java.util.List;

public interface QuestionRepository {

    // 질문 상태를 기준으로 질문 가져오기
    List<Question> findByQuestionStatus(QuestionStatus questionStatus);

    // 질문 저장
    Question save(Question question);
}
