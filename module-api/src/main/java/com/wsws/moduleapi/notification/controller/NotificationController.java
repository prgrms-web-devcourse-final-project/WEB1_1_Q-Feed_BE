package com.wsws.moduleapi.notification.controller;

import com.wsws.moduleapi.notification.dto.NotificationApiResponse;
import com.wsws.moduleapplication.notification.dto.NotificationServiceResponse;
import com.wsws.moduleapplication.notification.service.NotificationService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = " 알림 조회", description = "로그인한 사용자의 알림을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<NotificationApiResponse>> getUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String recipientId = userPrincipal.getId();
        List<NotificationServiceResponse> serviceResponses = notificationService.getNotifications(recipientId);

        // Application 계층의 DTO를 API 전용 DTO로 변환
        List<NotificationApiResponse> apiResponses = serviceResponses.stream()
                .map(NotificationApiResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(apiResponses);
    }

    @Operation(summary = "개별 알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("알림이 읽음 처리되었습니다.");
    }


    @Operation(summary = "전체 알림 읽음 처리", description = "로그인한 사용자의 모든 알림을 읽음 상태로 변경합니다.")
    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String recipientId = userPrincipal.getId();
        notificationService.markAllAsRead(recipientId);
        return ResponseEntity.ok("모든 알림이 읽음 처리되었습니다.");
    }
}
