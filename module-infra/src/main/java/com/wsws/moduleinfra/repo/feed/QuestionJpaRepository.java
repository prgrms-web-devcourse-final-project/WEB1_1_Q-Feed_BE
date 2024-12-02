package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduleinfra.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long>, QuestionRepository{

}
