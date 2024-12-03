package com.wsws.moduledomain.feed.answer.repo;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.question.Question;

import java.util.Optional;

public interface AnswerRepository {

    /**
     * 답변을 Id를 기준으로 찾기
     */
    Optional<Answer> findByAnswerId(Long id);

    /**
     * 답변 저장
     */
    Answer save(Answer answer, Question question);

    /**
     * 답변 수정
     */
    void edit(Answer answer);

    /**
     * 답변 삭제
     */
    void deleteById(Long id);
}
