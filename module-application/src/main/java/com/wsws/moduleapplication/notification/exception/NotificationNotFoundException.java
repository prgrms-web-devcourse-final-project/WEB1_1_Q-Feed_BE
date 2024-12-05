package com.wsws.moduleapplication.notification.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class NotificationNotFoundException extends ApplicationException {
    public static final NotificationNotFoundException EXCEPTION = new NotificationNotFoundException();

    private NotificationNotFoundException() {

        super(NotificationServiceErrorCode.NOTIFICATION_NOT_FOUND);
    }
}


