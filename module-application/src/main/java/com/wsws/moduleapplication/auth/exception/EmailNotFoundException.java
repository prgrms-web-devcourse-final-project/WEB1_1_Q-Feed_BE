package com.wsws.moduleapplication.auth.exception;

import com.wsws.moduleapi.exception.ApplicationException;

public class EmailNotFoundException extends ApplicationException {
    public static final EmailNotFoundException EXCEPTION = new EmailNotFoundException();

    private EmailNotFoundException() {
        super(AuthServiceErrorCode.EMAIL_NOT_FOUND);
    }
}
