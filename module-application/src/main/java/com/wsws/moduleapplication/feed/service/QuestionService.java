package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;
import com.wsws.moduleapplication.feed.exception.QuestionNotFoundException;
import com.wsws.moduledomain.cache.CacheManager;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CacheManager cacheManager;

    /**
     * 카테고리로 질문 조회
     */
    public QuestionFindServiceResponse findQuestionByCategoryId(Long categoryId) {

        // 캐시에서 데이터 조회
        String questionCacheKey = "QUESTION:" + LocalDate.now() + ":CATEGORY:" + categoryId; // 예: QUESTION:2024-12-16:CATEGORY:1

        Question cachedQuestion = cacheManager.get(questionCacheKey, Question.class);

        // 캐시에 데이터가 있다면 해당 데이터 반환
        if (cachedQuestion != null)
            return new QuestionFindServiceResponse(cachedQuestion);

        Question question = questionRepository.findDailyQuestionByCategoryId(categoryId)
                .orElseThrow(() -> QuestionNotFoundException.EXCEPTION);

        // 캐시에 데이터가 없다면 캐시에 해당 Question 저장
        if(question.getQuestionDate().equals(LocalDate.now())) // 만약 질문 생성이 실패된 경우, 어제 질문이 오늘의 질문으로 캐시에 저장되는 현상을 방지
            cacheManager.set(questionCacheKey, question, 24 * 60);

        return new QuestionFindServiceResponse(question);
    }

}