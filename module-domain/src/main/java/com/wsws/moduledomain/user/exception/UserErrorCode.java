package com.wsws.moduledomain.user.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;


@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    INVALID_EMAIL_FORMAT(BAD_REQUEST, "USER_400_2", "유효하지 않은 이메일 형식입니다."),
    INVALID_NICKNAME_FORMAT(BAD_REQUEST, "USER_400_3", "닉네임 형식이 올바르지 않습니다."),
    INVALID_PASSWORD_FORMAT(BAD_REQUEST, "USER_400_8", "비밀번호 형식이 올바르지 않습니다.");
    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
