package com.wsws.moduledomain.feed.comment.repo;

import com.wsws.moduledomain.feed.comment.AnswerComment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerCommentRepository {
    /**
     * id로 답변 댓글 찾아오기
     */
    Optional<AnswerComment> findById(Long id);

    /**
     * 답변 댓글 저장
     */
    AnswerComment save(AnswerComment answerComment);

    /**
     * 답변 댓글 수정
     */
    void edit(AnswerComment answerComment);
}
