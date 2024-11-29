package com.wsws.moduledomain.auth.exception;

import com.wsws.moduleapi.exception.BaseErrorCode;
import com.wsws.moduleapi.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.moduleapi.constants.ErrorCodeConstants.UNAUTHORIZED;

@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    AUTHENTICATION_FAILED(UNAUTHORIZED, "AUTH_401_1", "로그인에 실패했습니다."),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "AUTH_401_2", "Refresh Token이 유효하지 않습니다. 다시 로그인 해주세요."),
    REFRESH_TOKEN_IS_EXPIRED(UNAUTHORIZED, "AUTH_401_3", "Refresh Token이 만료되었습니다."),
    PASSWORD_MISMATCH(UNAUTHORIZED, "AUTH_401_4", "비밀번호가 일치하지 않습니다.");



    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
