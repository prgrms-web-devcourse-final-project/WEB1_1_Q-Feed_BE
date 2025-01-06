package com.wsws.moduledomain.admincontext.report.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;

@RequiredArgsConstructor
public enum AdminErrorCode implements BaseErrorCode {

    EMPTY_REPORTED_USER(BAD_REQUEST,"REPORT_400_1", "잘못된 신고 대상입니다."),
    EMPTY_REPORT(BAD_REQUEST,"REPORT_400_2", "잘못된 신고 사유입니다.");



    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
