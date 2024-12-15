package com.wsws.moduleapplication.authcontext.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class PasswordMismatchException extends ApplicationException {
    public static final PasswordMismatchException EXCEPTION = new PasswordMismatchException();

    private PasswordMismatchException() {
        super(AuthServiceErrorCode.PASSWORD_MISMATCH);
    }
}
