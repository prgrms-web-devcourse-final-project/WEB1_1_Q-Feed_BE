package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;
import com.wsws.moduleapplication.feed.exception.QuestionNotFoundException;
import com.wsws.moduledomain.cache.CacheManager;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CacheManager cacheManager;

    /**
     * 카테고리로 질문 조회
     */
    public QuestionFindServiceResponse findQuestionByCategoryId(Long categoryId) {

        // 캐시 키 생성
        String questionCacheKey = "QUESTION:" + LocalDate.now() + ":CATEGORY:" + categoryId; // 예: QUESTION:2024-12-16:CATEGORY:1

        // 캐시에서 데이터 조회
        QuestionFindServiceResponse cachedQuestion = cacheManager.get(questionCacheKey, QuestionFindServiceResponse.class);

        // 캐시에 데이터가 있다면 해당 데이터 반환
        if (cachedQuestion != null) {
            log.info("cache hit: {}", cachedQuestion);
            return cachedQuestion;
        }

        Question question = questionRepository.findDailyQuestionByCategoryId(categoryId)
                .orElseThrow(() -> QuestionNotFoundException.EXCEPTION);

        QuestionFindServiceResponse serviceResponse = new QuestionFindServiceResponse(question);

        // 캐시에 데이터가 없다면 캐시에 해당 Question 저장
        if(question.getQuestionDate().equals(LocalDate.now())) // 만약 질문 생성이 실패된 경우, 어제 질문이 오늘의 질문으로 캐시에 저장되는 현상을 방지
            cacheManager.set(questionCacheKey, serviceResponse, 24 * 60);

        return serviceResponse;
    }

}