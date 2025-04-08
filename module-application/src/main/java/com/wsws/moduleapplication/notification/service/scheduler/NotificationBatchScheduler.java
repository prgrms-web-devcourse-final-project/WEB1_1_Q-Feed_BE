package com.wsws.moduleapplication.notification.service.scheduler;

import com.wsws.moduleexternalapi.fcm.service.FcmService;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import com.wsws.moduleinfra.FcmRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationBatchScheduler {

    private final FcmService fcmService;
    private final FcmRedis fcmRedis;

    @Scheduled(fixedRate = 60000)
    public void sendBatchNotifications() {
        Set<String> keys = fcmRedis.getAllNotificationKeys();

        for (String key : keys) {
            String userId = key.replace("FCM_NOTIFICATION_", "");
            List<String> notis = fcmRedis.getNotifications(userId);

            if (notis == null || notis.isEmpty()) continue;

            String firstMessage = notis.get(0);
            int extra = notis.size() - 1;

            String finalMessage = firstMessage;
            if (extra > 0) {
                finalMessage += " 외 " + extra + "개의 알림이 더 있습니다.";
            }

            fcmService.sendFcm(userId, finalMessage, FcmType.GENERAL);
            fcmRedis.deleteNotifications(userId);
        }
    }
}

