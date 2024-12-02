package com.wsws.moduleapi.notification.controller;

import com.wsws.moduleapi.notification.dto.NotificationApiResponse;
import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;
import com.wsws.moduleapplication.notification.service.NotificationService;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationApiResponse>> getUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String recipientId = userPrincipal.getId();
        List<NotificationServiceResponse> serviceResponses = notificationService.getUnreadNotifications(recipientId);

        // Application 계층의 DTO를 API 전용 DTO로 변환
        List<NotificationApiResponse> apiResponses = serviceResponses.stream()
                .map(NotificationApiResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(apiResponses);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("알림이 읽음 처리되었습니다.");
    }

    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead(@RequestParam String recipientId) {
        notificationService.markAllAsRead(recipientId);
        return ResponseEntity.ok("모든 알림이 읽음 처리되었습니다.");
    }
}
