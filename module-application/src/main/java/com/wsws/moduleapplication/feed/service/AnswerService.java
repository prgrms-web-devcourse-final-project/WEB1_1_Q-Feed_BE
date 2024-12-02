package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.AnswerServiceResponse;
import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerServiceResponse findAnswerByAnswerId(Long answerId) {
        Answer answer = answerRepository.findByAnswerId(answerId).orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        return null;
    }
}
