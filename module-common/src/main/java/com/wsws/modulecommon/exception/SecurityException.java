package com.wsws.modulecommon.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.CustomException;

public class SecurityException extends CustomException {
    public SecurityException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public SecurityException(BaseErrorCode errorCode) {
        super(errorCode, "Security Module Exception");
    }
}



