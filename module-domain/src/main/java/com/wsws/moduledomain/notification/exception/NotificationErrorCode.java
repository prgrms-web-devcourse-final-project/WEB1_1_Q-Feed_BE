package com.wsws.moduledomain.notification.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;

@RequiredArgsConstructor
public enum NotificationErrorCode implements BaseErrorCode {

    EMPTY_NOTIFICATION_CONTENT(BAD_REQUEST, "NOTIFICATION_400_1", "알림 내용은 비어 있을 수 없습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}