package com.wsws.moduleapplication.authcontext.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class InvalidPasswordResetCodeException extends ApplicationException {
    public static final InvalidPasswordResetCodeException EXCEPTION = new InvalidPasswordResetCodeException();

    private InvalidPasswordResetCodeException() {
        super(AuthServiceErrorCode.INVALID_PASSWORD_RESET_CODE);
    }
}
