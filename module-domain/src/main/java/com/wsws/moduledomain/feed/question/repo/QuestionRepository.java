package com.wsws.moduledomain.feed.question.repo;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    // 질문 상태를 기준으로 질문 가져오기
    List<Question> findByQuestionStatus(QuestionStatus questionStatus);

    // 질문 저장
    Question save(Question question);

    // 카테고리를 기준으로 질문 가져오기
    Optional<Question> findByCategoryId(Long id);
}
