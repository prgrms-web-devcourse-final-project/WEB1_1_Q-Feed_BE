package com.wsws.moduleapplication.feed.service;

import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduledomain.feed.question.ai.QuestionGenerateClient;
import com.wsws.moduledomain.feed.question.ai.VectorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionAIService {

    private final VectorClient vectorClient; // 벡터 데이터베이스
    private final QuestionGenerateClient questionGenerateClient; // 질문 생성 AI

    private List<String> categories;
    private Map<String, Set<String>> questionBlackListMap; // 카테고리 별로 중복된 질문을 담는 블랙 리스트
    private Map<String, String> questionTempStore; // 카테고리 별로 검증이 끝난 질문 임시 저장소


    /**
     * 질문 생성 및 검증
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
        log.info(" 사용된 누적 토큰 수: [입력토큰: {}, 출력토큰: {}, 총합: {}]", questionGenerateClient.getGenerationTokens(), questionGenerateClient.getPromptTokens(), questionGenerateClient.getTotalTokens());
        return questionTempStore;
    }

    /**
     * 유사 질문 검색
     */
    private List<String> findSimilarText(String question) {
        return vectorClient.findSimilarText(question);
    }

    /**
     * AI 질문 생성
     */
    private Map<String, String> generateQuestions(List<String> categories, Map<String, Set<String>> questionBlackListMap) {
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


