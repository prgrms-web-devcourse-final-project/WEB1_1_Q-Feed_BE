package com.wsws.moduledomain.notification.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NotificationContent {

    private String content;

    public NotificationContent(String content) {
        if (content == null || content.isBlank()){
            throw new IllegalArgumentException("알림 내용은 비어 있습니다.");
        }
        this.content = content;
    }
}

