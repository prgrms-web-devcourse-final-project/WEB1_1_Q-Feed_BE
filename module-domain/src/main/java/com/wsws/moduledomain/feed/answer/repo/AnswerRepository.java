package com.wsws.moduledomain.feed.answer.repo;

import com.wsws.moduledomain.feed.answer.Answer;

import java.util.Optional;

public interface AnswerRepository {

    Optional<Answer> findByAnswerId(Long id);
}
