package com.wsws.moduleapi.exception;

public class ApiException extends CustomException{
    public ApiException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ApiException(BaseErrorCode errorCode) {
       super(errorCode, "Api Layer Exception");
    }
}
