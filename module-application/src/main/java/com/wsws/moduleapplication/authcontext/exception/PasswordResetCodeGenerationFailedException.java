package com.wsws.moduleapplication.authcontext.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class PasswordResetCodeGenerationFailedException extends ApplicationException {
    public static final PasswordResetCodeGenerationFailedException EXCEPTION = new PasswordResetCodeGenerationFailedException();

    private PasswordResetCodeGenerationFailedException() {
        super(AuthServiceErrorCode.PASSWORD_RESET_CODE_GENERATION_FAILED);
    }
}
