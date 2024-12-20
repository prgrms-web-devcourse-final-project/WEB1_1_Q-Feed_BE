package com.wsws.moduleapplication.authcontext.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class EmailNotFoundException extends ApplicationException {
    public static final EmailNotFoundException EXCEPTION = new EmailNotFoundException();

    private EmailNotFoundException() {
        super(AuthServiceErrorCode.EMAIL_NOT_FOUND);
    }
}
