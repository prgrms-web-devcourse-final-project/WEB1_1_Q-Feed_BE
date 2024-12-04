package com.wsws.moduleexternalapi.fcm.dto;


public record fcmRequestDto(
        String title,             // 알림 제목
        String body              // 알림 내용
) {
    public fcmRequestDto(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
