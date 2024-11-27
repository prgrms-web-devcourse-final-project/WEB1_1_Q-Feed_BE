package com.wsws.moduleapi.exception;

import lombok.RequiredArgsConstructor;

import static com.wsws.moduleapi.constants.ErrorCodeConstants.*;

@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    VALIDATION_ERROR(400, "VALIDATION_ERROR_400", "잘못된 입력 값입니다."),
    PERMISSION_DENIED(403, "PERMISSION_DENIED_401", "해당 API 권한이 없습니다"),
    FILE_NOT_FOUND(404, "FILE_NOT_FOUND_404", "해당 파일이 존재하지 않습니다"),
    OTHER_SERVER_BAD_REQUEST(BAD_REQUEST, "AUTH_OTHER_400", "Other server bad request"),
    OTHER_SERVER_UNAUTHORIZED(UNAUTHORIZED, "AUTH_OTHER_401", "Other server unauthorized"),
    OTHER_SERVER_FORBIDDEN(FORBIDDEN, "AUTH_OTHER_403", "Other server forbidden"),
    OTHER_SERVER_EXPIRED_TOKEN(BAD_REQUEST, "AUTH_OTHER_400", "Other server expired token"),
    OTHER_SERVER_NOT_FOUND(BAD_REQUEST, "AUTH_OTHER_400", "Other server not found error"),
    OTHER_SERVER_INTERNAL_SERVER_ERROR(
            BAD_REQUEST, "FEIGN_400_6", "Other server internal server error");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
