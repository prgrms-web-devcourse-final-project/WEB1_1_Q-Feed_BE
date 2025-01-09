package com.wsws.moduleapplication.notification.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class NoUnreadNotificationsException extends ApplicationException {
    public static final NoUnreadNotificationsException EXCEPTION = new NoUnreadNotificationsException();

    private NoUnreadNotificationsException() {
        super(NotificationServiceErrorCode.NO_UNREAD_NOTIFICATIONS);
    }
}
