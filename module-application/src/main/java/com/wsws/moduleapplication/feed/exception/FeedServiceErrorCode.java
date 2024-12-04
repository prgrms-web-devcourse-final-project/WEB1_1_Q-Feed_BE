package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.*;

@RequiredArgsConstructor
public enum FeedServiceErrorCode implements BaseErrorCode {

    QUESTION_NOT_FOUND(NOT_FOUND, "FEED_APP_404_1", "존재하지 않는 질문입니다."),
    ANSWER_NOT_FOUND(NOT_FOUND, "FEED_APP_404_2", "존재하지 않는 답변입니다."),
    ANSWER_COMMENT_NOT_FOUND(NOT_FOUND, "FEED_APP_404_3", "존재하지 않는 답변 댓글입니다."),
    FILE_SIZE_EXCEEDED(BAD_REQUEST, "FEED_APP_400_1","파일 크기가 초과되었습니다."),
    UNSUPPORTED_FILE_FORMAT(BAD_REQUEST, "FEED_APP_400_2","지원하지 않는 파일 형식입니다."),
    FILE_PROCESSING_ERROR(INTERNAL_SERVER, "FEED_APP_500_1", "파일 처리 중 오류가 발생했습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}