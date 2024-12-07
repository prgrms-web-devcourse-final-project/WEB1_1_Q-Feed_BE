package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAnswerRepository extends JpaRepository<AnswerEntity, Long> {

//    @Query("select a from AnswerEntity a join fetch a.answerCommentEntities where a.id = :id")
//    Optional<AnswerEntity> findByIdWithComments(Long id);

    @Query("SELECT a FROM AnswerEntity a WHERE a.createdAt < :answerCursor ORDER BY a.createdAt DESC")
    List<AnswerEntity> findAllWithCursor(LocalDateTime answerCursor, Pageable pageable);


    Long countByUserIdAndVisibilityTrue(String userId);
    Long countByUserIdAndVisibilityFalse(String userId);
}
