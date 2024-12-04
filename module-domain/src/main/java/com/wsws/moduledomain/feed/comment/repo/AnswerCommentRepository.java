package com.wsws.moduledomain.feed.comment.repo;

import com.wsws.moduledomain.feed.comment.AnswerComment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerCommentRepository {
    Optional<AnswerComment> findById(Long id);
}
