package com.wsws.moduleapplication.feed.service;

import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.ai.QuestionGenerateClient;
import com.wsws.moduledomain.feed.question.ai.VectorClient;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionAIService {

    private final VectorClient vectorClient; // 벡터 데이터베이스
    private final QuestionGenerateClient questionGenerateClient; // 질문 생성 AI
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    private List<String> categories;
    private Map<String, Set<String>> questionBlackListMap; // 카테고리 별로 중복된 질문을 담는 블랙 리스트
    private Map<String, String> questionTempStore; // 카테고리 별로 검증이 끝난 질문 임시 저장소

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
                            LocalDate.now().plusDays(1),
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

    /**
     * 질문 생성 및
     */
    public Map<String, String> generateAndValidateQuestions() {
        initList(); // 리스트 초기화

        while (!categories.isEmpty()) {
            Map<String, String> createdQuestions = generateQuestions(categories, questionBlackListMap); // AI로 부터 질문 생성

            for (String categoryName : createdQuestions.keySet()) {
                String question = createdQuestions.get(categoryName);
                List<String> similarQuestions = findSimilarText(question); // 유사 질문 검색

                // 질문 검증
                if (similarQuestions.isEmpty()) {
                    log.info("질문 검증 완료: {}: {}", categoryName, question);
                    questionTempStore.put(categoryName, question);
                    removeCategoryFromList(categoryName);
                } else {
                    log.info("질문 중복: {}: {}", categoryName, question);
                    addQuestionsToBlackListMap(categoryName, question, similarQuestions);
                }
            }
        }

        return questionTempStore;
    }

    /**
     * 유사 질문 검색
     */
    public List<String> findSimilarText(String question) {
        return vectorClient.findSimilarText(question);
    }

    /**
     * AI 질문 생성
     */
    public Map<String, String> generateQuestions(List<String> categories, Map<String, Set<String>> questionBlackListMap) {
        return questionGenerateClient.generateQuestions(categories, questionBlackListMap);
    }


    /**
     * 카테고리 리스트 초기화
     */
    private void initList() {

        categories = new CopyOnWriteArrayList<>(Arrays.stream(CategoryName.values())
                .map(Enum::name)
                .toList());
        questionBlackListMap = new ConcurrentHashMap<>();
        questionTempStore = new ConcurrentHashMap<>();
    }

    private void addQuestionsToBlackListMap(String categoryName, String question, List<String> similarQuestions) {
        questionBlackListMap.computeIfAbsent(categoryName, k -> new HashSet<>()).add(question);
        questionBlackListMap.get(categoryName).addAll(similarQuestions); // 중복된 질문들을 블랙 리스트에 저장
    }

    /**
     * 해당 카테고리 리스트에서 제외
     */
    private void removeCategoryFromList(String categoryName) {
        categories.remove(categoryName);
        questionBlackListMap.remove(categoryName);
    }
}


