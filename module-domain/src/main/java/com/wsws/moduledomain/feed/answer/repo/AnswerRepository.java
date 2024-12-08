package com.wsws.moduledomain.feed.answer.repo;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.dto.AnswerQuestionDTO;
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
    List<Answer> findAllByCategoryIdWithCursor(LocalDateTime cursor, int size, Long categoryId);

    /**
     * 답변 리스트를 UserId 기준으로 페이징 해서 찾기
     */
    List<AnswerQuestionDTO> findAllByUserIdWithCursor(String userId, LocalDateTime cursor, int size, boolean isMine);

    /**
     * 특정 사용자의 답변의 갯수
     */
    Long countByUserId(String userId, boolean isMine);

    /**
     * 특정 사용자의 특정 질문에 대한 답변
     */
    Optional<Answer> findAnswerByUserIdAndQuestionId(String userId, Long questionId);

    /**
     * 답변 저장
     */
    Answer save(Answer answer);

    /**
     * 특정 사용자가 특정 질문에 대해 작성한 글이 있는지
     */
    boolean existsByUserIdAndQuestionId(String userId, Long questionId);

    /**
     * 답변 수정
     */
    void edit(Answer answer);

    /**
     * 답변 삭제
     */
    void deleteById(Long id);
}