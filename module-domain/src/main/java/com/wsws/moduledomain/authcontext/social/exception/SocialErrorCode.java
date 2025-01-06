package com.wsws.moduledomain.authcontext.social.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;

@RequiredArgsConstructor
public enum SocialErrorCode implements BaseErrorCode {

    EMPTY_PROVIDER(BAD_REQUEST, "SOCIAL_400_1","잘못된 provider입니다"),
    EMPTY_PROVIDER_ID(BAD_REQUEST, "SOCIAL_400_2","잘못된 provider_id입니다");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
