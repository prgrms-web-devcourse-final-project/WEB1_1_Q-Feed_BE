package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.*;

@RequiredArgsConstructor
public enum ChatServiceErrorCode implements BaseErrorCode {
    ALREADY_CHATROOM(BAD_REQUEST, "CHATROOM_400_1", "이미 존재하는 채팅방이 있습니다."),
    CHATROOM_NOT_FOUND(NOT_FOUND, "CHATROOM_400_2", "채팅방을 찾을 수 없습니다."),
    CHAT_RECEIVER_NOT_FOUND(NOT_FOUND, "CHATROOM_400_3", "채팅중인 상대방을 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS_CHAT(NOT_FOUND, "CHATROOM_400_4", "수정할 수 있는 권한이 없습니다."),
    FILE_UPLOAD_REQUIRED(BAD_REQUEST, "CHATROOM_400_5","파일을 업로드해야 합니다."),
    FILE_SIZE_EXCEEDED(BAD_REQUEST, "CHATROOM_400_6","파일 크기가 초과되었습니다."),
    UNSUPPORTED_FILE_FORMAT(BAD_REQUEST, "CHATROOM_400_7","지원하지 않는 파일 형식입니다."),
    FILE_PROCESSING_ERROR(INTERNAL_SERVER, "CHATROOM_400_8", "파일 처리 중 오류가 발생했습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
