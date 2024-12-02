package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.QuestionServiceResponse;
import com.wsws.moduleapplication.feed.exception.QuestionNotFoundException;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * 카테고리로 질문 조회
     */
    public QuestionServiceResponse findQuestionByCategoryId(Long id) {
        Question question = questionRepository.findDailyQuestionByCategoryId(id)
                .orElseThrow(() -> QuestionNotFoundException.EXCEPTION);
        return new QuestionServiceResponse(question);
    }


}
