package com.wsws.moduleapi.exception;

public class InfraException extends CustomException{
    public InfraException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InfraException(BaseErrorCode errorCode) {
        super(errorCode, "Infra Layer Exception");
    }
}
