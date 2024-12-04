package com.wsws.moduleexternalapi.fcm.dto;

public record fcmRequestDto(
        String title,             // 알림 제목
        String body             // 알림 내용

) {}
