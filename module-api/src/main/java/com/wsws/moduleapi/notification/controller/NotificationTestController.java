package com.wsws.moduleapi.notification.controller;

import com.wsws.moduleapi.notification.dto.NotificationTestRequest;
import com.wsws.moduleapplication.notification.service.NotificationService;
import com.wsws.moduleapi.notification.util.ReflectionNotificationInvoker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationTestController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendTestNotification(@RequestBody NotificationTestRequest request) {
        try {
            ReflectionNotificationInvoker.invokeSendNotification(
                    notificationService,
                    request.senderId(),
                    request.recipientId(),
                    "/test-url",
                    request.type()
            );

            return ResponseEntity.ok("테스트 알림 전송 완료됨");
        } catch (ClassNotFoundException e) {
            log.error("FcmType 클래스를 찾을 수 없습니다. type={}", request.type(), e);
            return ResponseEntity.internalServerError().body("FcmType 클래스 오류");
        } catch (IllegalArgumentException e) {
            log.error("존재하지 않는 FcmType: {}", request.type(), e);
            return ResponseEntity.badRequest().body("잘못된 FcmType");
        } catch (Exception e) {
            log.error("테스트 알림 전송 실패: type={}", request.type(), e);
            return ResponseEntity.internalServerError().body("테스트 실패");
        }
    }
}
