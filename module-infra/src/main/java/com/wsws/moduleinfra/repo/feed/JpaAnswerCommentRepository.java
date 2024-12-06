package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduleinfra.entity.feed.AnswerCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaAnswerCommentRepository extends JpaRepository<AnswerCommentEntity, Long> {

    @Query("SELECT ac FROM AnswerCommentEntity ac WHERE ac.answerEntity.id = :answerId AND ac.parentCommentEntity IS NULL")
    List<AnswerCommentEntity> findParentCommentsByAnswerId(Long answerId);

    @Query("SELECT ac FROM AnswerCommentEntity ac WHERE ac.parentCommentEntity.id IN :parentIds")
    List<AnswerCommentEntity> findChildCommentsByParentsId(List<Long> parentIds);
}
