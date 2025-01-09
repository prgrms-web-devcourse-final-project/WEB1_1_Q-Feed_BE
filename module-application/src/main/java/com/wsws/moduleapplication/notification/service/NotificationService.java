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
import com.wsws.moduleexternalapi.fcm.dto.fcmRequestDto;
import com.wsws.moduleexternalapi.fcm.service.FcmService;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import com.wsws.moduleinfra.FcmRedis;
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

    // ID 생성
    private final AtomicLong notificationIdGenerator = new AtomicLong(1);

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
                .orElseThrow(() -> NotificationNotFoundException.EXCEPTION);

        if (notification.isRead()) {
            throw NotificationAlreadyReadException.EXCEPTION;
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
            throw NoUnreadNotificationsException.EXCEPTION;
        }

        notificationRepository.markAllAsReadByRecipientId(recipientId);
    }

    // 알림 생성 및 저장
    public void sendNotification(String senderId, String recipientId, Long targetId, Long commentId, Long groupId, String url, FcmType fcmType) {
        User sender = userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> SenderNotFoundException.EXCEPTION);
        User recipient = userRepository.findById(UserId.of(recipientId))
                .orElseThrow(() -> RecipientNotFoundException.EXCEPTION);


        Long notificationId = notificationIdGenerator.getAndIncrement();


        // 알림 내용 생성
        String title = fcmService.makeFcmTitle(fcmType.getType());
        String body = createNotificationBody(fcmType, sender.getNickname().getValue());

        fcmRequestDto fcmDTO = fcmService.makeFcmDTO(title, body);

        // FCM 전송
        fcmService.fcmSend(sender.getNickname().getValue(), fcmDTO);
        // 알림 저장
        Notification notification = Notification.create(
                notificationId,
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

    // FcmType 에 따른 알림 본문 생성
    private String createNotificationBody(FcmType fcmType, String sender) {
        return switch (fcmType) {
            case FOLLOW -> fcmService.makeFollowBody(sender, fcmType.getType());
            case CHAT -> fcmService.makeChatBody(sender, fcmType.getType());
            case ANSWER_COMMENT -> fcmService.makeCommentBody(sender, fcmType.getType());
            case ANSWER_LIKE -> fcmService.makeAnswerLikeBody(sender, fcmType.getType());
            case COMMENT_LIKE -> fcmService.makeCommentLikeBody(sender, fcmType.getType());
            case Q_SPACE_POST_COMMENT -> fcmService.makeQCommentBody(sender, fcmType.getType());
            case Q_SPACE_POST_LIKE -> fcmService.makeQPostLikeBody(sender, fcmType.getType());
            case Q_SPACE_COMMENT_LIKE -> fcmService.makeQCommentLikeBody(sender, fcmType.getType());
        };
    }

    public void saveFcmToken(SaveFcmTokenRequest request, String userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        String value = request.fcmToken();
        Duration twoMonths = Duration.ofDays(60); // 2달

        // Redis 저장 시도
        log.info("Redis에 FCM 토큰 저장 시도!! userId={}, fcmToken={}", userId, value);

        fcmRedis.saveFcmToken(String.valueOf(user.getId()), value, twoMonths);

        // Redis 저장 완료
        log.info("Redis에 FCM 토큰 저장 완료!! userId={}, fcmToken={}", userId, value);
    }

    // FCM 토큰 삭제 로직
    public void deleteFcmToken(String userId) {

        // Redis에서 토큰 삭제
        fcmRedis.deleteFcmToken(String.valueOf(userId));
        log.info("FCM 토큰 삭제 완료 > userId={}", userId);

        // Redis에서 해당 토큰이 존재하는지 확인 (삭제확인 테스트용)`
        String token = fcmRedis.getFcmToken(String.valueOf(userId));
        if (token == null) {
            log.info("FCM 토큰 삭제 확인 완료 > userId={}", userId);
        } else {
            log.warn("FCM 토큰 삭제 실패 > userId={}, token={}", userId, token);
        }
    }




}
