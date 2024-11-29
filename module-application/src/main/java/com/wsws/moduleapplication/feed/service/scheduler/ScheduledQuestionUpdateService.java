package com.wsws.moduleapplication.feed.service.scheduler;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduledQuestionUpdateService {
    private final QuestionRepository questionRepository;

    /**
     * 매일 자정에 Question 테이블의 Question Status 컬럼 변경
     * 어제 질문들은 isToday: ACTIVATED -> INACTICATED
     * 오늘 질문들은 isToday: CREATED -> ACTIVATED
     */
    @Scheduled(cron = "0 0 0 * * ?") // 매일 00시 00분에 실행되도록 설정
    public void updateQuestions() {
        // 어제 질문들 비활성화
        questionRepository.findByQuestionStatus(QuestionStatus.ACTIVATED)
                .forEach(Question::inactivateQuestion);

        // 오늘 질문들 활성화
        questionRepository.findByQuestionStatus(QuestionStatus.CREATED)
                .forEach(Question::activateQuestion);

        log.info("스케줄링 작업 완료");
    }
}
