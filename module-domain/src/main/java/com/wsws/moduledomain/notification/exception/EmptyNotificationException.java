package com.wsws.moduledomain.notification.exception;

import com.wsws.modulecommon.exception.DomainException;
import com.wsws.moduledomain.notification.exception.NotificationErrorCode;

public class EmptyNotificationException extends DomainException {

    public static final EmptyNotificationException EXCEPTION = new EmptyNotificationException();

    private EmptyNotificationException() {
        super(NotificationErrorCode.EMPTY_NOTIFICATION_CONTENT);
    }
}
