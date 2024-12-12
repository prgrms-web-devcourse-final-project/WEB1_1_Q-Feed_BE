package com.wsws.moduleapplication.authcontext.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class VerificationCodeGenerationFailedException extends ApplicationException {
    public static final VerificationCodeGenerationFailedException EXCEPTION = new VerificationCodeGenerationFailedException();

    private VerificationCodeGenerationFailedException() {
        super(AuthServiceErrorCode.VERIFICATION_CODE_GENERATION_FAILED);
    }
}
