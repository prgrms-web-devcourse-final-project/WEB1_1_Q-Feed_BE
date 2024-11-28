package com.wsws.moduleapi.exception;

public class SecurityException extends CustomException {
    public SecurityException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public SecurityException(BaseErrorCode errorCode) {
        super(errorCode, "Security Module Exception");
    }
}



