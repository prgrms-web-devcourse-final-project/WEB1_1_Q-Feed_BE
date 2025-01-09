package com.wsws.moduleapplication.notification.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;
import static com.wsws.modulecommon.constants.ErrorCodeConstants.NOT_FOUND;

@RequiredArgsConstructor
public enum NotificationServiceErrorCode implements BaseErrorCode {
    NOTIFICATION_NOT_FOUND(NOT_FOUND, "NOTIFICATION_404_1", "해당 알림을 찾을 수 없습니다."),
    NOTIFICATION_ALREADY_READ(BAD_REQUEST, "NOTIFICATION_400_1", "이미 읽음 처리된 알림입니다."),
    NO_UNREAD_NOTIFICATIONS(BAD_REQUEST, "NOTIFICATION_400_2", "읽지 않은 알림이 없습니다."),
    RECIPIENT_NOT_FOUND(NOT_FOUND, "NOTIFICATION_404_2", "수신자를 찾을 수 없습니다."),
    SENDER_NOT_FOUND(NOT_FOUND, "NOTIFICATION_404_3", "발신자를 찾을 수 없습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
