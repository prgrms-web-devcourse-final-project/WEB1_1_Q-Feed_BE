package com.wsws.modulecommon.exception;

public record ErrorInfo(Integer status, String errorCode, String message) {
    public static ErrorInfo of(Integer status, String errorCode, String message) {
        return new ErrorInfo(status, errorCode, message);
    }
}
