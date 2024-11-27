package com.wsws.moduleapi.exception;

public class ExternalApiException extends CustomException{
    public ExternalApiException(BaseErrorCode errorCode, String sourceLayer) {
        super(errorCode, sourceLayer);
    }

    public ExternalApiException(BaseErrorCode errorCode) {
        super(errorCode, "ExternalApi Layer Exception");
    }
}
