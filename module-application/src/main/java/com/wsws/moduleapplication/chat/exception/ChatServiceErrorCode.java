package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;
import static com.wsws.modulecommon.constants.ErrorCodeConstants.NOT_FOUND;

@RequiredArgsConstructor
public enum ChatServiceErrorCode implements BaseErrorCode {
    ALREADY_CHATROOM(BAD_REQUEST, "CHATROOM_400_1", "이미 존재하는 채팅방이 있습니다."),
    CHATROOM_NOT_FOUND(NOT_FOUND, "CHATROOM_400_2", "채팅방을 찾을 수 없습니다."),
    FILE_UPLOAD_REQUIRED(BAD_REQUEST, "CHATROOM_400_3","파일을 업로드해야 합니다."),
    FILE_SIZE_EXCEEDED(BAD_REQUEST, "CHATROOM_400_4","파일 크기가 초과되었습니다."),
    UNSUPPORTED_FILE_FORMAT(BAD_REQUEST, "CHATROOM_400_5","지원하지 않는 파일 형식입니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
