package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long>{
    List<QuestionEntity> findByQuestionStatus(QuestionStatus questionStatus);

    @Query("select qe from QuestionEntity qe where qe.categoryId = :categoryId and qe.questionStatus = 'ACTIVATED'")
    Optional<QuestionEntity> findDailyQuestionByCategoryId(Long categoryId);
}
