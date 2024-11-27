package com.wsws.moduleapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class CustomException extends RuntimeException {

    protected final BaseErrorCode errorCode;
    protected final String sourceLayer;

    public Integer getStatus() {
        return errorCode.getErrorInfo().status();
    }

    public String getMessage() {
        if (sourceLayer == null) {
            return errorCode.getErrorInfo().message();
        } else {
            return String.format("%s - %s", sourceLayer, errorCode.getErrorInfo().message());
        }
    }
}
