package com.wsws.moduleapplication.notification.service;

import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.dto.NotificationDto;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;


    // 모든 알림 목록 출력 (읽음/안읽음 포함)
    @Transactional
    public List<NotificationServiceResponse> getNotifications(String recipientId) {
        return notificationRepository.findByRecipientId(recipientId).stream()
                .map(NotificationServiceResponse::new)
                .toList();
    }

    // 개별 읽음 처리
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않습니다."));

        if (notification.isRead()) {
            throw new IllegalStateException("이미 읽음 처리된 알림입니다.");
        }

        notification.markAsRead();
        // 읽음 상태 반영
        notificationRepository.edit(notification);
    }

    // 전체 읽음 처리
    @Transactional
    public void markAllAsRead(String recipientId) {
        List<NotificationDto> unreadNotifications = notificationRepository.findByRecipientIdAndIsReadFalse(recipientId);

        if (unreadNotifications.isEmpty()) {
            throw new IllegalStateException("읽지 않은 알림이 없거나 알림이 존재하지 않습니다.");
        }

        notificationRepository.markAllAsReadByRecipientId(recipientId);
    }
}
