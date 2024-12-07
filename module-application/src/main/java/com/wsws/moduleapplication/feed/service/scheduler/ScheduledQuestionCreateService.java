package com.wsws.moduleapplication.feed.service.scheduler;

import com.wsws.moduleapplication.feed.service.QuestionAIService;
import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduledomain.feed.question.ai.QuestionGenerateClient;
import com.wsws.moduledomain.feed.question.ai.VectorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledQuestionCreateService {

    private final QuestionAIService questionAIService; // AI 질문 관련 서비스

    private List<String> categories;
    private Map<String, Set<String>> questionBlackListMap; // 카테고리 별로 중복된 질문을 담는 블랙 리스트
    private Map<String, String> questionTempStore; // 카테고리 별로 검증이 끝난 질문 임시 저장소

    /**
     * 매일 23시 30분에 질문을 생성
     */

    @Scheduled(cron = "0 30 23 * * ?")
    public void createQuestion() {
        int maxRetries = 10; // 최대 재시도 횟수
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                initList(); // 리스트 초기화

                // TODO: while문을 빠져나올 조건
                while (!categories.isEmpty()) {
                    Map<String, String> createdQuestions = questionAIService.createQuestions(categories, questionBlackListMap);

                    for (String categoryName : createdQuestions.keySet()) {
                        String question = createdQuestions.get(categoryName);
                        List<String> similarQuestions = questionAIService.findSimilarText(question);

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
                log.info("모든 질문 생성완료.");
                questionAIService.saveQuestions(questionTempStore);
                break; // 성공 시 루프 종료
            } catch (Exception e) {
                log.error("createQuestion 실패. 시도 횟수: {}", attempt, e);
                if (attempt >= maxRetries) {
                    log.error("모든 재시도가 실패했습니다.");
                    // TODO: 이메일로 알림
                }
            }
        }
    }


    private void addQuestionsToBlackListMap(String categoryName, String question, List<String> similarQuestions) {
        questionBlackListMap.computeIfAbsent(categoryName, k -> new HashSet<>()).add(question);
        questionBlackListMap.get(categoryName).addAll(similarQuestions); // 중복된 질문들을 블랙 리스트에 저장
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

    /**
     * 해당 카테고리 리스트에서 제외
     */
    private void removeCategoryFromList(String categoryName) {
        categories.remove(categoryName);
        questionBlackListMap.remove(categoryName);
    }


}
