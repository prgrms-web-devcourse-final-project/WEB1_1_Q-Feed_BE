package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.question.QuestionFindServiceResponse;
import com.wsws.moduleapplication.feed.exception.QuestionNotFoundException;
import com.wsws.moduledomain.cache.CacheManager;
import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.ai.VectorClient;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final VectorClient vectorClient; // 벡터 데이터베이스
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

    /**
     * 데이터베이스에 질문 저장
     * 질문 저장은 모든 질문들의 검증이 끝난 후에 이루어진다.
     * -> 저장해야하는 데이터베이스가 두 개이기 때문에 데이터 불일치를 방지하기 위함.
     */
    @Transactional
    public void saveQuestions(Map<String, String> questions) {
        log.info("데이터베이스에 질문을 저장");

        List<Category> categoryList = categoryRepository.findAllCategories(); // 카테고리 전부 로드
        // RDBMS에 질문 저장
        for (String categoryName : questions.keySet()) {
            Category category = categoryList.stream()
                    .filter(c -> categoryName.equals(c.getCategoryName().name()))
                    .findFirst().orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));

            questionRepository.save(
                    Question.create(
                            null,
                            questions.get(categoryName),
                            QuestionStatus.CREATED,
                            LocalDate.now().plusDays(1),
                            category.getId().getValue()
                    )
            );
        }

        // 벡터 데이터베이스에 질문 저장
        vectorClient.store(questions.values().stream().toList());
    }

    @Transactional
    public void updateQuestionStatus() {
        List<Question> dailyQuestions = questionRepository.findByQuestionStatus(QuestionStatus.CREATED);

        // CREATED 상태 질문이 없다는 것은 질문 생성이 실패했다는 뜻이므로 갱신 작업을 진행하면 안됨.
        if (dailyQuestions.isEmpty()) return;

        // 어제 질문들 비활성화
        questionRepository.findByQuestionStatus(QuestionStatus.ACTIVATED)
                .forEach(question -> {
                    question.inactivateQuestion();
                    questionRepository.edit(question);
                });

        // 오늘 질문들 활성화
        dailyQuestions
                .forEach(question -> {
                    question.activateQuestion();
                    questionRepository.edit(question);
                });

    }

}