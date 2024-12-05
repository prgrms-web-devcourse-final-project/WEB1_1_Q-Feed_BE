package com.wsws.moduledomain.notification.vo;

import com.wsws.modulecommon.exception.DomainException;
import com.wsws.moduledomain.notification.exception.EmptyNotificationException;
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
            throw EmptyNotificationException.EXCEPTION;
        }
        this.content = content;
    }
}

