package com.wsws.moduleapplication.notification.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class RecipientNotFoundException extends ApplicationException {
    public static final RecipientNotFoundException EXCEPTION = new RecipientNotFoundException();

    private RecipientNotFoundException() {
        super(NotificationServiceErrorCode.RECIPIENT_NOT_FOUND);
    }
}
