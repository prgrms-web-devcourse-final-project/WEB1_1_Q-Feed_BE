package com.wsws.moduleapplication.feed.service.scheduler;

import com.wsws.moduleapplication.feed.service.QuestionAIService;
import com.wsws.moduleapplication.feed.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledQuestionCreateService {

    private final QuestionAIService questionAIService; // AI 질문 관련 서비스
    private final QuestionService questionService;

    /**
     * 매일 23시 30분에 질문을 생성
     */

    @Scheduled(cron = "0 30 23 * * ?", zone = "Asia/Seoul")
    public void createQuestionsScheduling() {
        log.info("질문 생성 스케줄링 시작");
        int maxRetries = 10; // 최대 재시도 횟수
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;

                Map<String, String> questionsMap = questionAIService.generateAndValidateQuestions();// 질문 생성 및 검증

                log.info("모든 질문 생성완료.");
                questionService.saveQuestions(questionsMap); // 질문 저장
                log.info("질문 생성 스케줄링 성공");
                break; // 성공 시 루프 종료
            } catch (Exception e) {
                log.error("질문 생성 스케줄링 실패. 시도 횟수: {}", attempt, e);
                if (attempt >= maxRetries) {
                    log.error("모든 재시도가 실패했습니다.");
                    // TODO: 이메일로 알림
                }
            }
        }
    }
}
