package com.wsws.moduleapplication.notification.service.scheduler;

import com.wsws.moduledomain.notification.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledNotificationService {
    private final NotificationRepository notificationRepository;


    // 하루에 한 번, 7일 이상된 알림 데이터를 삭제하는 스케줄러

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void deleteOldNotifications() {
        try {
            log.info("7일 이상된 알림 데이터를 삭제하는 작업 시작");

            int deletedCount = notificationRepository.deleteNotificationsOlderThan(7);
            log.info("삭제된 알림 데이터 개수: {}", deletedCount);

        } catch (Exception e) {
            log.error("7일 이상된 알림 데이터 삭제 중 오류 발생", e);
        }
    }
}