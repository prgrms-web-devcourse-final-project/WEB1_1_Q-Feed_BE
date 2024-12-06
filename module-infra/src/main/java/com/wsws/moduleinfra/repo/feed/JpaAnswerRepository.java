package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAnswerRepository extends JpaRepository<AnswerEntity, Long> {

//    @Query("select a from AnswerEntity a join fetch a.answerCommentEntities where a.id = :id")
//    Optional<AnswerEntity> findByIdWithComments(Long id);
}
