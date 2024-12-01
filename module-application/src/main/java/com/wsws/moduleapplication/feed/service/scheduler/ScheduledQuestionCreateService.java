package com.wsws.moduleapplication.feed.service.scheduler;

import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import com.wsws.moduleexternalapi.feed.client.QuestionGenerateClient;
import com.wsws.moduleexternalapi.feed.client.RedisVectorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.wsws.moduledomain.category.vo.CategoryName.*;
import static com.wsws.moduleexternalapi.feed.util.TokenCalculateUtil.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduledQuestionCreateService {

    private final RedisVectorClient redisVectorClient; // 벡터 데이터베이스
    private final QuestionGenerateClient questionGenerateClient; // 질문 생성 AI
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

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

                while (!categories.isEmpty()) {
                    Map<String, String> createdQuestions = questionGenerateClient.createQuestions(categories, questionBlackListMap);

                    for (String categoryName : createdQuestions.keySet()) {
                        String question = createdQuestions.get(categoryName);
                        List<String> similarQuestions = redisVectorClient.findSimilarText(question);

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
                saveQuestion();
                log.info(" 사용된 누적 토큰 수: [입력토큰: {}, 출력토큰: {}, 총합: {}]", getPromptToken(), getGenerationToken(), getTotalToken());
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
        categories = new CopyOnWriteArrayList<>(List.of());
        questionBlackListMap = new ConcurrentHashMap<>();
        questionTempStore = new ConcurrentHashMap<>();
    }

    /**
     * 데이터베이스에 질문 저장
     * 질문 저장은 모든 질문들의 검증이 끝난 후에 이루어진다.
     * -> 저장해야하는 데이터베이스가 두 개이기 때문에 데이터 불일치를 방지하기 위함.
     */
    private void saveQuestion() {
        log.info("데이터베이스에 질문을 저장");

        // 벡터 데이터베이스에 질문 저장
        redisVectorClient.store(questionTempStore.values().stream().toList());

        List<Category> categoryList = categoryRepository.findAll(); // 카테고리를 전부 받아옴.

        // RDBMS에 질문 저장
        for (String categoryName : questionTempStore.keySet()) {

            Category category = categoryList.stream()
                    .filter(c -> categoryName.equals(c.getCategoryName().name()))
                    .findFirst().orElseThrow(RuntimeException::new); // 해당 카테고리이름의 카테고리 객체를 찾음

            questionRepository.save(
                    Question.create(questionTempStore.get(categoryName), QuestionStatus.CREATED, LocalDateTime.now(), category.getId())); // Question 엔티티 RDBMS에 저장
        }
    }

    /**
     * 해당 카테고리 리스트에서 제외
     */
    private void removeCategoryFromList(String categoryName) {
        categories.remove(categoryName);
        questionBlackListMap.remove(categoryName);
    }


}
