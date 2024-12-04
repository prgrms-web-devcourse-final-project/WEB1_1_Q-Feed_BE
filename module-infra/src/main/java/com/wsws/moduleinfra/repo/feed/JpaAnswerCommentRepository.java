package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduleinfra.entity.feed.AnswerCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerCommentRepository extends JpaRepository<AnswerCommentEntity, Long> {
}
