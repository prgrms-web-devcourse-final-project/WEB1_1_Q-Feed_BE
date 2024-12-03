package com.wsws.moduleapplication.feed.service.scheduler;

import com.wsws.moduleapplication.feed.service.QuestionAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledQuestionUpdateService {
    private final QuestionAIService questionAIService;

    /**
     * 매일 자정에 Question 테이블의 Question Status 컬럼 변경
     * 어제 질문들은 isToday: ACTIVATED -> INACTICATED
     * 오늘 질문들은 isToday: CREATED -> ACTIVATED
     */
    @Scheduled(cron = "0 0 0 * * ?") // 매일 00시 00분에 실행되도록 설정
    public void updateQuestions() {
        int maxRetries = 3; // 최대 재시도 횟수
        int attempt = 0;    // 현재 시도 횟수

        while (attempt < maxRetries) {
            try {
                attempt++;
                log.info("스케줄링 작업 실행, 시도 횟수: {}", attempt);

                // 질문 상태 업데이트 로직
                questionAIService.updateQuestions();

                // 성공하면 루프 종료
                log.info("스케줄링 작업 완료, 시도 횟수: {}", attempt);
                break;
            } catch (Exception e) {
                log.error("스케줄링 작업 실패, 시도 횟수: {}, 에러: {}", attempt, e.getMessage());

                // 마지막 시도에서 실패했을 때 추가 처리
                if (attempt >= maxRetries) {
                    log.error("스케줄링 작업 모든 재시도 실패. ");
                    // TODO: 이메일로 알림
                }
            }
        }
    }
}
