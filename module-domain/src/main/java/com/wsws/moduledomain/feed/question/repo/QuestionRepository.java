package com.wsws.moduledomain.feed.question.repo;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    // 질문 상태를 기준으로 질문 가져오기
    List<Question> findByQuestionStatus(QuestionStatus questionStatus);

    // Id를 기준으로 질문 가져오기
    Optional<Question> findById(Long id);

    // 질문 저장
    Question save(Question question);

    // 질문 상태 업데이트
    void edit(Question question);

    // 카테고리를 기준으로 오늘 질문 가져오기
    Optional<Question> findDailyQuestionByCategoryId(Long categoryId);
}
