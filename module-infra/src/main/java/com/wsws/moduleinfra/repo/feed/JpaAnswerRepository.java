package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAnswerRepository extends JpaRepository<AnswerEntity, Long> {

    // 락을 걸고 해당 Answer 가져오기
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AnswerEntity a WHERE a.id = :id")
    Optional<AnswerEntity> findByIdWithLock(Long id);

    // 특정 사용자의 특정 질문에 대한 답변
    @Query("""
            SELECT a 
            FROM AnswerEntity a join a.questionEntity q 
            WHERE a.userId = :userId 
            AND q.id= :questionId 
            """)
    Optional<AnswerEntity> findAnswerByUserIdAndQuestionId(String userId, Long questionId);

    @Query("SELECT COUNT(a) > 0 FROM AnswerEntity a WHERE a.userId = :userId AND a.questionEntity.id = :questionId")
    boolean existsByUserIdAndQuestionId(String userId, Long questionId);

}
