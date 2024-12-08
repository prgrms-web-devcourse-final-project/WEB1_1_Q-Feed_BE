package com.wsws.moduleapplication.feed.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.*;

@RequiredArgsConstructor
public enum FeedServiceErrorCode implements BaseErrorCode {

    QUESTION_NOT_FOUND(NOT_FOUND, "FEED_APP_404_1", "존재하지 않는 질문입니다."),
    ANSWER_NOT_FOUND(NOT_FOUND, "FEED_APP_404_2", "존재하지 않는 답변입니다."),
    ANSWER_CHANGE_NOT_ALLOWED(FORBIDDEN, "FEED_APP_403_1","해당 답변을 수정할 수 있는 권한이 없습니다."),
    ANSWER_COMMENT_CHANGE_NOT_ALLOWED(FORBIDDEN, "FEED_APP_403_2","해당 답변 댓글을 변경할 수 있는 권한이 없습니다."),
    ANSWER_COMMENT_NOT_FOUND(NOT_FOUND, "FEED_APP_404_3", "존재하지 않는 답변 댓글입니다."),
    PARENT_ANSWER_COMMENT_NOT_FOUND(NOT_FOUND, "FEED_APP_404_4", "없는 부모 답변 댓글입니다."),
    FILE_SIZE_EXCEEDED(BAD_REQUEST, "FEED_APP_400_1","파일 크기가 초과되었습니다."),
    UNSUPPORTED_FILE_FORMAT(BAD_REQUEST, "FEED_APP_400_2","지원하지 않는 파일 형식입니다."),
    ALREADY_ANSWER_WRITTEN(CONFLICT, "FEED_APP_409_1", "이미 답변을 작성한적이 있는 질문입니다."),
    FILE_PROCESSING_ERROR(INTERNAL_SERVER, "FEED_APP_500_1", "파일 처리 중 오류가 발생했습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}