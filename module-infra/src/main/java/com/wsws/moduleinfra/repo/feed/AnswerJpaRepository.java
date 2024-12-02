package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {
}
