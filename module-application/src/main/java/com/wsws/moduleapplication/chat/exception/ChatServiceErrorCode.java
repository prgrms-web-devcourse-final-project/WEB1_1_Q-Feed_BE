package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;

@RequiredArgsConstructor
public enum ChatServiceErrorCode implements BaseErrorCode {
    ALREADY_CHATROOM(BAD_REQUEST, "CHATROOM_400_1", "이미 존재하는 채팅방이 있습니다."),
    CHATROOM_NOT_FOUND(BAD_REQUEST, "CHATROOM_400_2", "채팅방을 찾을 수 없습니다.");
    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
