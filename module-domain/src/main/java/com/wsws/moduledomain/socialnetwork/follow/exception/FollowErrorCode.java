package com.wsws.moduledomain.socialnetwork.follow.exception;


import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.BAD_REQUEST;

@RequiredArgsConstructor
public enum FollowErrorCode implements BaseErrorCode {

    SELF_FOLLOW_NOT_ALLOWED(BAD_REQUEST, "FOLLOW_400_1", "자기 자신을 팔로우할 수 없습니다."),
    EMPTY_FOLLOWER_ID(BAD_REQUEST, "FOLLOW_400_2", "잘못된 follower_id 입니다."),
    EMPTY_FOLLOWEE_ID(BAD_REQUEST, "FOLLOW_400_3", "잘못된 followee_id 입니다.");

    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
