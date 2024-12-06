package com.wsws.moduleinfra.storage;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;


@RequiredArgsConstructor
public enum S3ErrorCode implements BaseErrorCode {

    FILE_UPLOAD_FAILED(BAD_REQUEST, "FILE_400_1", "파일 업로드에 실패했습니다."),
    EMPTY_FILE(BAD_REQUEST, "FILE_400_2", "업로드할 파일이 비어있습니다."),
    FILE_SIZE_EXCEEDED(BAD_REQUEST, "FILE_400_3", "파일 크기가 허용된 최대 크기를 초과했습니다.");
    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
