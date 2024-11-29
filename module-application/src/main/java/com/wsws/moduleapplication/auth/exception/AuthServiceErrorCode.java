package com.wsws.moduleapplication.auth.exception;


import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.UNAUTHORIZED;

@RequiredArgsConstructor
public enum AuthServiceErrorCode implements BaseErrorCode {

    EMAIL_NOT_FOUND(UNAUTHORIZED, "AUTH_401_1", "존재하지 않는 이메일입니다."),
    PASSWORD_MISMATCH(UNAUTHORIZED, "AUTH_401_2", "비밀번호가 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "AUTH_401_3", "유효하지 않은 Refresh Token입니다."),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED, "AUTH_401_4", "Refresh Token이 만료되었습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}