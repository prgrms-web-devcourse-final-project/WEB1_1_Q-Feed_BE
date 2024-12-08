package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.dto.AnswerQuestionDTO;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaAnswerRepository extends JpaRepository<AnswerEntity, Long> {

    @Query("SELECT a FROM AnswerEntity a WHERE a.createdAt < :answerCursor ORDER BY a.createdAt DESC")
    List<AnswerEntity> findAllWithCursor(LocalDateTime answerCursor, Pageable pageable);


    Long countByUserIdAndVisibilityTrue(String userId);
    Long countByUserId(String userId);

    @Query("""
            SELECT a 
            FROM AnswerEntity a join fetch a.questionEntity q 
            WHERE a.userId = :userId 
            AND a.createdAt < :answerCursor 
            ORDER BY a.createdAt DESC
            """)
    List<AnswerEntity> findAllByUserIdWithCursor(String userId, LocalDateTime answerCursor, Pageable pageable);
    @Query("""
            SELECT a
            FROM AnswerEntity a join fetch a.questionEntity q 
            WHERE a.userId = :userId 
            AND a.createdAt < :answerCursor 
            AND a.visibility = true 
            ORDER BY a.createdAt DESC
            """)
    List<AnswerEntity> findAllByUserIdAndVisibilityTrueWithCursor(String userId, LocalDateTime answerCursor, Pageable pageable);

}
