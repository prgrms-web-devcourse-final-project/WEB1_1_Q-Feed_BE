package com.wsws.moduleapplication.notification.service;

import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.dto.NotificationDto;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleexternalapi.fcm.dto.fcmRequestDto;
import com.wsws.moduleexternalapi.fcm.service.FcmService;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;
    private final UserRepository userRepository;

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

    // 알림 생성 및 저장
    public void sendNotification(String senderId, String recipientId, Long targetId, Long commentId, Long groupId, String url, FcmType fcmType) {
        User sender = userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> new IllegalArgumentException("발신자를 찾을 수 없습니다."));
        User recipient = userRepository.findById(UserId.of(recipientId))
                .orElseThrow(() -> new IllegalArgumentException("수신자를 찾을 수 없습니다."));

        // 알림 내용 생성
        String title = fcmService.makeFcmTitle(fcmType.getType());
        String body = fcmService.makeQLikeBody(sender.getNickname().getValue(),fcmType.getType());
        fcmRequestDto fcmDTO = fcmService.makeFcmDTO(title, body);

        // FCM 전송
        fcmService.fcmSend(sender.getNickname().getValue(), fcmDTO);

        // 알림 저장
        Notification notification = Notification.create(
                null,
                fcmType.getType(),
                recipient.getId().getValue(),
                sender.getId().getValue(),
                fcmDTO.body(), // FCM 전송 body
                targetId,
                commentId,
                groupId,
                url
        );
        notificationRepository.save(notification);
    }
}
