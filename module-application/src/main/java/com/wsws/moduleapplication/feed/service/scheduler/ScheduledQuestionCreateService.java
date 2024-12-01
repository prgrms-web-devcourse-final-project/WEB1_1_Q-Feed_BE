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
    private Map<String, List<String>> questionBlackList;
    private Map<String, String> questionTempStore; // 검증이 끝난 질문 임시 저장소

    /**
     * 매일 23시 30분에 질문을 생성
     */
    @Scheduled(cron = "0 30 23 * * ?")
    public void createQuestion() {
        initList(); // 리스트 초기화

        // 모든 카테고리의 질문들이 생성될 때 까지 반복
        while (!categories.isEmpty()) {
            Map<String, String> createdQuestions = questionGenerateClient.createQuestions(categories, questionBlackList);

            // 질문 검증 및 저장
            for (String categoryName : createdQuestions.keySet()) {
                String question = createdQuestions.get(categoryName);
                List<String> similarQuestions = redisVectorClient.findSimilarText(question);

                if (similarQuestions.isEmpty()) { // 중복되지 않는 질문일 때,
                    log.info("질문 검증 완료: {}: {}", categoryName, question);
                    questionTempStore.put(categoryName, question); // 질문 리스트에 임시 저장
                    removeCategoryFromList(categoryName); // 저장한 질문의 카테고리는 리스트에서 제외
                } else { // 중복된 질문이면
                    // 질문 블랙 리스트에 추가
                    log.info("질문 중복: {}: {}", categoryName, question);
                    if (questionBlackList.containsKey(categoryName)) { // 생성된 질문 블랙리스트에 저장
                        questionBlackList.get(categoryName).add(question);
                    } else {
                        questionBlackList.put(categoryName, new ArrayList<>(List.of(question)));
                    }
                    questionBlackList.get(categoryName).addAll(similarQuestions); // 중복된 질문들을 블랙 리스트에 저장
                }
            }
        }
        log.info("모든 질문 생성완료.");
        saveQuestion(); // 데이터베이스에 질문 저장.
        log.info(" 사용된 누적 토큰 수: [입력토큰: {}, 출력토큰: {}, 총합: {}]", getPromptToken(), getGenerationToken(), getTotalToken());
    }


    /**
     * 카테고리 리스트 초기화
     */
    private void initList() {
        categories = new ArrayList<>(List.of(TRAVEL.name(), DELICIOUS_RESTAURANT.name(), MOVIE.name(), MUSIC.name(), READING.name(), SPORTS.name()));
        questionBlackList = new HashMap<>();
        questionTempStore = new HashMap<>();
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

        // RDBMS에 질문 저장
        for (String c : questionTempStore.keySet()) {
            Category category = categoryRepository.findByCategoryName(CategoryName.valueOf(c)); // 카테고리에 ID를 받아옴.
            questionRepository.save(Question.create(questionTempStore.get(c), QuestionStatus.CREATED, LocalDateTime.now(), category.getId())); // Question 엔티티 RDBMS에 저장
        }
    }

    /**
     * 해당 카테고리 리스트에서 제외
     */
    private void removeCategoryFromList(String categoryName) {
        categories.remove(categoryName);
        questionBlackList.remove(categoryName);
    }


}
