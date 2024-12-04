package com.wsws.moduleexternalapi.fcm.dto;

public record FCMRequestDto(
        String title,             // 알림 제목
        String body,              // 알림 내용
        String recipientFcmToken // 수신자 FCM 토큰
) {}
