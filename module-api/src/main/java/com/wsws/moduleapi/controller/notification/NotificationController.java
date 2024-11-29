package com.wsws.moduleapi.controller.notification;

import com.wsws.moduleapplication.dto.notification.NotificationServiceRequestDto;
import com.wsws.moduleapplication.service.notification.NotificationService;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduleinfra.repo.notification.NotificationReadRepository;
import com.wsws.moduleinfra.repo.notification.dto.NotificationResponseInfraDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationReadRepository readRepository;

    // 알림 생성
    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody NotificationServiceRequestDto requestDto) {
        notificationService.createNotification(
                requestDto.recipientId(),
                requestDto.type(),
                requestDto.content(),
                requestDto.senderId()
                //fcm토큰 추가
        );
        return ResponseEntity.ok().build();
    }

    // 알림 목록 가져오기 (읽은 처리 안된 알람들 출력)
    @GetMapping("/{recipientId}")
    public ResponseEntity<List<NotificationResponseInfraDto>> getUnreadNotifications(
            @PathVariable UserId recipientId
    ) {
        List<NotificationResponseInfraDto> notifications = readRepository.findByRecipientIdAndIsReadFalse(recipientId);
        return ResponseEntity.ok(notifications);
    }

    // 개별 읽음 처리
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> MarkAsRead(@PathVariable Long notificationId){
        notificationService.markAsRead(notificationId);
        return ResponseEntity.status(200).body("알림이 읽음 처리되었습니다.");
    }

    // 전체 읽음 처리
    @PutMapping("/{recipientId}/read-all")
    public ResponseEntity<String> MarkAsReadAll(@PathVariable UserId recipientId){
        notificationService.markAllAsRead(recipientId);
        return ResponseEntity.status(200).body("알림이 읽음 처리되었습니다.");
    }
}
