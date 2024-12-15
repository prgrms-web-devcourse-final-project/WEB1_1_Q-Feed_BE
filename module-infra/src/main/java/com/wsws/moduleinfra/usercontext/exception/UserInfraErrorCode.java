package com.wsws.moduleinfra.usercontext.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.INTERNAL_SERVER;

@RequiredArgsConstructor
public enum UserInfraErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(INTERNAL_SERVER, "USER_INFRA_500_1", "해당 이메일로 사용자를 찾을 수 없습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
