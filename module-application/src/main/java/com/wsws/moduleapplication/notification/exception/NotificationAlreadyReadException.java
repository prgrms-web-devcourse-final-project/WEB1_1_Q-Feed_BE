package com.wsws.moduleapplication.notification.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class NotificationAlreadyReadException extends ApplicationException {
    public static final NotificationAlreadyReadException EXCEPTION = new NotificationAlreadyReadException();

    private NotificationAlreadyReadException() {
        super(NotificationServiceErrorCode.NOTIFICATION_ALREADY_READ);
    }
}
