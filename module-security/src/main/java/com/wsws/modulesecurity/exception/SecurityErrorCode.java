package com.wsws.modulesecurity.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.UNAUTHORIZED;

@RequiredArgsConstructor
public enum SecurityErrorCode implements BaseErrorCode {

    INVALID_TOKEN(UNAUTHORIZED, "SECURITY_401_1", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(UNAUTHORIZED, "SECURITY_401_2", "만료된 토큰입니다."),
    USER_NOT_FOUND(UNAUTHORIZED, "SECURITY_401_3", "사용자를 찾을 수 없습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
