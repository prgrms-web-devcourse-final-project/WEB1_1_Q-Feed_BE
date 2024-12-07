package com.wsws.moduledomain.feed.answer.repo;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.question.Question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

    /**
     * 답변을 Id를 기준으로 찾기
     */
    Optional<Answer> findById(Long id);

    /**
     * 답변 리스트를 페이징 해서 찾기
     */
    List<Answer> findAllWithCursor(LocalDateTime commentCursor, int size);

    /**
     * 답변을 UserId 기준으로 찾기
     */
    List<Answer> findByUserId(String userId);

    /**
     * 특정 사용자의 답변의 갯수
     */
    Long countByUserId(String userId, boolean isMine);

    /**
     * 답변 저장
     */
    Answer save(Answer answer);

    /**
     * 답변 수정
     */
    void edit(Answer answer);

    /**
     * 답변 삭제
     */
    void deleteById(Long id);
}