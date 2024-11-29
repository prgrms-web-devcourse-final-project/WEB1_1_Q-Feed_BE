package com.wsws.moduleapplication.follow.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;
import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;

@RequiredArgsConstructor
public enum FollowServiceErrorCode implements BaseErrorCode {
    ALREADY_FOLLOWED(BAD_REQUEST, "FOLLOW_400_1", "이미 팔로우한 사용자입니다."),
    FOLLOW_NOT_FOUND(BAD_REQUEST, "FOLLOW_400_2", "팔로우 관계를 찾을 수 없습니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
