package com.wsws.moduleinfra.feed.repo;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface QuestionJpaRepository extends QuestionRepository, JpaRepository<Question, Long> {

}
