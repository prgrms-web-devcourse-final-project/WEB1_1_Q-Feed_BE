package com.wsws.moduleexternalapi.fcm.dto;


public record FCMRequestDto(
        String title,             // 알림 제목
        String body              // 알림 내용
) {
    public FCMRequestDto(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
