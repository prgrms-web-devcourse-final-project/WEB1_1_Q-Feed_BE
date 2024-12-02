package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduleinfra.repo.feed.mapper.AnswerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final AnswerJpaRepository answerJpaRepository;

    @Override
    public Optional<Answer> findByAnswerId(Long id) {

        return answerJpaRepository.findById(id)
                .map(AnswerMapper::toDomain);
    }
}
