package com.wsws.moduleapplication.notification.service;

import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 목록 출력
    @Transactional
    public List<NotificationServiceResponse> getUnreadNotifications(String recipientId) {
        return notificationRepository.findByRecipientIdAndIsReadFalse(recipientId).stream()
                .map(NotificationServiceResponse::new)
                .toList();
//                .collect(Collectors.toList());
    }

    // 개별 읽음 처리
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않습니다."));
        notification.markAsRead();
        // 읽음 상태 반영
        notificationRepository.edit(notification);
    }

    // 전체 읽음 처리
    @Transactional
    public void markAllAsRead(String recipientId) {
        notificationRepository.markAllAsReadByRecipientId(recipientId);

    }


}
