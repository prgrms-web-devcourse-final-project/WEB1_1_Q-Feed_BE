package com.wsws.moduledomain.notification.vo;

public class NotificationId {
    private final Long value;

    private NotificationId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Notification ID는 0보다 큰 값이어야 합니다.");
        }
        this.value = value;
    }

    public static NotificationId of(Long value) {
        return new NotificationId(value);
    }

    public Long getValue() {
        return value;
    }
}

