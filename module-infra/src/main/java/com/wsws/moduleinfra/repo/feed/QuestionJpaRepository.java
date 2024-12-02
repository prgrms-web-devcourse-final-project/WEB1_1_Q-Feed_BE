package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long>{
    List<QuestionEntity> findByQuestionStatus(QuestionStatus questionStatus);
}
