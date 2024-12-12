package com.wsws.moduleapplication.authcontext.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class InvalidVerificationCodeException extends ApplicationException {
    public static final InvalidVerificationCodeException EXCEPTION = new InvalidVerificationCodeException();

    private InvalidVerificationCodeException() {
        super(AuthServiceErrorCode.INVALID_VERIFICATION_CODE);
    }
}
