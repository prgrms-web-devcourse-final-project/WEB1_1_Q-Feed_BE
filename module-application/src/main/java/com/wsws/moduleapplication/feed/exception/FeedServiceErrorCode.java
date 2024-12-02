package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.NOT_FOUND;

@RequiredArgsConstructor
public enum FeedServiceErrorCode implements BaseErrorCode {

    QUESTION_NOT_FOUND(NOT_FOUND, "FEED_APP_400_1", "존재하지 않는 질문입니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
