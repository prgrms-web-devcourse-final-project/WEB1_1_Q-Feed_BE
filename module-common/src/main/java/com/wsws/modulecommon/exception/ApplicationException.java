package com.wsws.modulecommon.exception;

public class ApplicationException extends CustomException{
    public ApplicationException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ApplicationException(BaseErrorCode errorCode) {
        super(errorCode, "Application Layer Exception");
    }
}
