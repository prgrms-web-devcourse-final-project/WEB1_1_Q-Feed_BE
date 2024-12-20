package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.dto.AnswerQuestionDTO;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAnswerRepository extends JpaRepository<AnswerEntity, Long> {

    // 락을 걸고 해당 Answer 가져오기
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AnswerEntity a WHERE a.id = :id")
    Optional<AnswerEntity> findByIdWithLock(Long id);

    // 카테고리 상관없이 답변 찾기
    @Query("""
            SELECT a 
            FROM AnswerEntity a join a.questionEntity q 
            WHERE q.questionStatus = 'ACTIVATED' 
            AND a.createdAt < :answerCursor 
            ORDER BY a.createdAt DESC
            """)
    List<AnswerEntity> findAllWithCursor(LocalDateTime answerCursor, Pageable pageable);

    // 특정 categoryId의 답변 찾기
    @Query("""
            SELECT a 
            FROM AnswerEntity a join a.questionEntity q
            WHERE q.questionStatus = 'ACTIVATED' 
            AND q.categoryId = :categoryId
            AND a.createdAt < :answerCursor 
            ORDER BY a.createdAt DESC
            """)
    List<AnswerEntity> findAllByCategoryIdWithCursor(LocalDateTime answerCursor, Pageable pageable, Long categoryId);


    // 특정 userId를 가진 답변의 갯수
    Long countByUserId(String userId);

    // 특정 userId를 가지고 visibility가 true인 답변의 갯수
    Long countByUserIdAndVisibilityTrue(String userId);

    // 특정 사용자의 특정 질문에 대한 답변
    @Query("""
            SELECT a 
            FROM AnswerEntity a join a.questionEntity q 
            WHERE a.userId = :userId 
            AND q.id= :questionId 
            """)
    Optional<AnswerEntity> findAnswerByUserIdAndQuestionId(String userId, Long questionId);


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

    @Query("SELECT COUNT(a) > 0 FROM AnswerEntity a WHERE a.userId = :userId AND a.questionEntity.id = :questionId")
    boolean existsByUserIdAndQuestionId(String userId, Long questionId);

}
