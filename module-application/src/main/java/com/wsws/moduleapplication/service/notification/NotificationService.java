package com.wsws.moduleapplication.service.notification;

import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.NotificationType;
import com.wsws.moduledomain.notification.repo.NotificationRepository;

import com.wsws.moduledomain.user.vo.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 생성
    @Transactional
    public void createNotification(UserId recipientId, NotificationType type, String content, UserId senderId ) {
        //fcm 토큰 적용 후 작성
    }

    // 알림 읽음 처리 (개별)
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()-> new IllegalArgumentException("해당 알림이 존재하지 않습니다."));
        notification.markAsRead();
    }

    // 알림 읽음 처리 (모든 알림)
    @Transactional
    public void markAllAsRead(UserId recipientId) {
        List<Notification> notifications = notificationRepository.findByRecipientIdAndIsReadFalse(recipientId);
        notifications.forEach(Notification::markAsRead);
    }
}

