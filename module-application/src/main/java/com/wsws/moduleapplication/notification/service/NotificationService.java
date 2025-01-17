package com.wsws.moduleapplication.notification.service;

import com.wsws.moduleapplication.notification.dto.SaveFcmTokenRequest;
import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;
import com.wsws.moduleapplication.notification.exception.*;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.dto.NotificationDto;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import com.wsws.moduleexternalapi.fcm.service.FcmService;
import com.wsws.moduleinfra.FcmRedis;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;
    private final UserRepository userRepository;
    private final FcmRedis fcmRedis;

    private final AtomicLong notificationIdGenerator = new AtomicLong(1);

    @Transactional
    public List<NotificationServiceResponse> getNotifications(String recipientId) {
        return notificationRepository.findByRecipientId(recipientId).stream()
                .map(NotificationServiceResponse::new)
                .toList();
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> NotificationNotFoundException.EXCEPTION);

        if (notification.isRead()) {
            throw NotificationAlreadyReadException.EXCEPTION;
        }

        notification.markAsRead();
        notificationRepository.edit(notification);
    }

    @Transactional
    public void markAllAsRead(String recipientId) {
        List<NotificationDto> unreadNotifications = notificationRepository.findByRecipientIdAndIsReadFalse(recipientId);

        if (unreadNotifications.isEmpty()) {
            throw NoUnreadNotificationsException.EXCEPTION;
        }

        notificationRepository.markAllAsReadByRecipientId(recipientId);
    }

    public void sendNotification(String senderId, String recipientId, Long targetId, Long commentId, Long groupId, String url, FcmType fcmType) {

        if (isSenderAndRecipientSame(senderId, recipientId)) {
            return; // 발신자 = 수신자 동일 -> 알림 전송 x
        }

        User sender = userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> SenderNotFoundException.EXCEPTION);
        User recipient = userRepository.findById(UserId.of(recipientId))
                .orElseThrow(() -> RecipientNotFoundException.EXCEPTION);

        Long notificationId = notificationIdGenerator.getAndIncrement();
        String content = fcmService.makeFcmBody(fcmType, sender.getNickname().getValue());

        // FCM 전송
        fcmService.fcmSend(
                recipient.getId().getValue(),
                fcmType,
                sender.getNickname().getValue()
        );

        if (shouldSkipNotificationStorage(fcmType)) {
            return; // CHAT는 알림 저장 x
        }

        // 알림 저장
        Notification notification = Notification.create(
                notificationId,
                fcmType.name(),
                sender.getId().getValue(),
                recipient.getId().getValue(),
                content,
                targetId,
                commentId,
                groupId,
                url
        );
        notificationRepository.save(notification);
    }

    public void saveFcmToken(SaveFcmTokenRequest request, String userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        String value = request.fcmToken();
        Duration twoMonths = Duration.ofDays(60);

        log.info("Redis에 FCM 토큰 저장 시도: userId={}, fcmToken={}", userId, value);
        fcmRedis.saveFcmToken(user.getId().getValue(), value, twoMonths);
        log.info("Redis에 FCM 토큰 저장 완료: userId={}, fcmToken={}", userId, value);
    }

    public void deleteFcmToken(String userId) {
        fcmRedis.deleteFcmToken(String.valueOf(userId));
        log.info("FCM 토큰 삭제 완료: userId={}", userId);

        String token = fcmRedis.getFcmToken(String.valueOf(userId));
        if (token == null) {
            log.info("FCM 토큰 삭제 확인 완료: userId={}", userId);
        } else {
            log.warn("FCM 토큰 삭제 실패: userId={}, token={}", userId, token);
        }
    }

    //발신자와 수신자가 동일한지 확인
    private boolean isSenderAndRecipientSame(String senderId, String recipientId) {
        if (senderId.equals(recipientId)) {
            log.info("발신자와 수신자가 동일하여 알림x : senderId={}, recipientId={}", senderId, recipientId);
            return true;
        }
        return false;
    }

    // CHAT 타입은 알림 저장 x
    private boolean shouldSkipNotificationStorage(FcmType fcmType) {
        if (fcmType == FcmType.CHAT) {
            log.info("CHAT 타입 알림은 저장하지 않습니다");
            return true;
        }
        return false;
    }


}
