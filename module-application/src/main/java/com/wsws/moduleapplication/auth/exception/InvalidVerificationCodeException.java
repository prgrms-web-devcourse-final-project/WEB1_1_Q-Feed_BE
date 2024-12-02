package com.wsws.moduleapplication.auth.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class InvalidVerificationCodeException extends ApplicationException {
    public static final InvalidVerificationCodeException EXCEPTION = new InvalidVerificationCodeException();

    private InvalidVerificationCodeException() {
        super(AuthServiceErrorCode.INVALID_VERIFICATION_CODE);
    }
}
