package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduleinfra.entity.feed.AnswerCommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaAnswerCommentRepository extends JpaRepository<AnswerCommentEntity, Long> {

    @Query("""
                SELECT ac 
                FROM AnswerCommentEntity ac 
                WHERE ac.answerEntity.id = :answerId 
                  AND (ac.parentCommentEntity.id IS NULL 
                      OR ac.id IN (
                          SELECT DISTINCT ac_.parentCommentEntity.id
                          FROM AnswerCommentEntity ac_
                          WHERE ac_.parentCommentEntity.id IS NOT NULL
                        )
                  )
                  AND ac.createdAt < :commentCursor
                ORDER BY ac.depth, ac.createdAt DESC 
            """)
    List<AnswerCommentEntity> findParentCommentsByAnswerIdWithCursor(Long answerId, LocalDateTime commentCursor, Pageable pageable);

    @Query("SELECT ac FROM AnswerCommentEntity ac WHERE ac.parentCommentEntity.id IN :parentIds")
    List<AnswerCommentEntity> findChildCommentsByParentsId(List<Long> parentIds);
}
