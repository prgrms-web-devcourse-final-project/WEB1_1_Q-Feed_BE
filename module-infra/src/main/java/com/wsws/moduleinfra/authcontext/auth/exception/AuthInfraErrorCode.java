package com.wsws.moduleinfra.authcontext.auth.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.INTERNAL_SERVER;

@RequiredArgsConstructor
public enum AuthInfraErrorCode implements BaseErrorCode {

    INVALID_REFRESH_TOKEN_DATA(INTERNAL_SERVER, "AUTH_INFRA_500_1", "RefreshTokenData가 유효하지 않습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
