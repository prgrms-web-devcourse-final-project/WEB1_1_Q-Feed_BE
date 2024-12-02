package com.wsws.moduledomain.chat.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;

@RequiredArgsConstructor
public enum ChatErrorCode implements BaseErrorCode {

    SELF_SELECTION_NOT_ALLOWED(BAD_REQUEST, "CHAT_400_1","자기 자신을 선택할 수 없습니다."),
    INVALID_CHAT_FORMAT(BAD_REQUEST, "CHAT_400_2", "유효하지 않은 채팅 형식입니다."),
    URL_REQUIRED(BAD_REQUEST, "CHAT_400_3", "URL이 필요합니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}