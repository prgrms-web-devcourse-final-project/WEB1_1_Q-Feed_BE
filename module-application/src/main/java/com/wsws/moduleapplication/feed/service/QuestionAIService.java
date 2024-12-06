package com.wsws.moduleapplication.feed.service;

import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.ai.QuestionGenerateClient;
import com.wsws.moduledomain.feed.question.ai.VectorClient;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionAIService {

    private final VectorClient vectorClient; // 벡터 데이터베이스
    private final QuestionGenerateClient questionGenerateClient; // 질문 생성 AI
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 데이터베이스에 질문 저장
     * 질문 저장은 모든 질문들의 검증이 끝난 후에 이루어진다.
     * -> 저장해야하는 데이터베이스가 두 개이기 때문에 데이터 불일치를 방지하기 위함.
     */
    @Transactional
    public void saveQuestions(Map<String, String> questionTempStore) {
        log.info("데이터베이스에 질문을 저장");

        List<Category> categoryList = categoryRepository.findAllCategories(); // 카테고리 전부 로드
        // RDBMS에 질문 저장
        for (String categoryName : questionTempStore.keySet()) {
            Category category = categoryList.stream()
                    .filter(c -> categoryName.equals(c.getCategoryName().name()))
                    .findFirst().orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));

            questionRepository.save(
                    Question.create(
                            null,
                            questionTempStore.get(categoryName),
                            QuestionStatus.CREATED,
                            LocalDateTime.now(),
                            category.getId().getValue()
                    )
            );
        }

        // 벡터 데이터베이스에 질문 저장
        vectorClient.store(questionTempStore.values().stream().toList());
        log.info(" 사용된 누적 토큰 수: [입력토큰: {}, 출력토큰: {}, 총합: {}]", questionGenerateClient.getGenerationTokens(), questionGenerateClient.getPromptTokens(), questionGenerateClient.getTotalTokens());
    }

    @Transactional
    public void updateQuestions() {
        // 어제 질문들 비활성화
        questionRepository.findByQuestionStatus(QuestionStatus.ACTIVATED)
                .forEach(q -> {
                    q.inactivateQuestion();
                    questionRepository.edit(q);
                });

        // 오늘 질문들 활성화
        questionRepository.findByQuestionStatus(QuestionStatus.CREATED)
                .forEach(q -> {
                    q.activateQuestion();
                    questionRepository.edit(q);
                });
    }

    public List<String> findSimilarText(String question) {
        return vectorClient.findSimilarText(question);
    }

    public Map<String, String> createQuestions(List<String> categories, Map<String, Set<String>> questionBlackListMap) {
        return questionGenerateClient.createQuestions(categories, questionBlackListMap);
    }
}


