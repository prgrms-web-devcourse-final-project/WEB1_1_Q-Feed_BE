package com.wsws.moduleapplication.auth.exception;

import com.wsws.moduleapi.exception.ApplicationException;

public class VerificationCodeGenerationFailedException extends ApplicationException {
    public static final VerificationCodeGenerationFailedException EXCEPTION = new VerificationCodeGenerationFailedException();

    private VerificationCodeGenerationFailedException() {
        super(AuthServiceErrorCode.VERIFICATION_CODE_GENERATION_FAILED);
    }
}
