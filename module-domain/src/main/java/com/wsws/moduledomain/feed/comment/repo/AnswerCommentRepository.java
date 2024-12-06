package com.wsws.moduledomain.feed.comment.repo;

import com.wsws.moduledomain.feed.comment.AnswerComment;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerCommentRepository {
    /**
     * id로 답변 댓글 찾아오기
     */
    Optional<AnswerComment> findById(Long id);

    /**
     * 특정 답변의 부모 댓글(자식이 존재하는 댓글) 찾아오기(페이징)
     */
    List<AnswerComment> findParentCommentsByAnswerIdWithCursor(Long answerId, LocalDateTime commentCursor, int size);

    /**
     * 특정 부모 댓글들의 하위 댓글 조회
     */
    List<AnswerComment> findChildCommentsByParentsId(List<Long> parentIds);

    /**
     * 특정 답변의 최상위 부모 댓글 갯수
     */
    int countParentCommentByAnswerId(Long answerId);

    /**
     * 답변 댓글 저장
     */
    AnswerComment save(AnswerComment answerComment);

    /**
     * 답변 댓글 수정
     */
    void edit(AnswerComment answerComment);

    /**
     * 답변 삭제
     */
    void deleteById(Long id);
}
