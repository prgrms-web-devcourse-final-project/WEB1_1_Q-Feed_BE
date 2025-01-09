package com.wsws.moduleapplication.group.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.*;

@RequiredArgsConstructor
public enum GroupServiceErrorCode implements BaseErrorCode {
    GROUP_NOT_FOUND(NOT_FOUND, "GROUP_404_1", "그룹을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(BAD_REQUEST, "GROUP_400_1", "존재하지 않는 멤버입니다."),
    UNAUTHORIZED_ACCESS(FORBIDDEN, "GROUP_403_1","권한이 없습니다. 관리자만 수정할 수 있습니다."),
    MEMBER_NOT_IN_GROUP(BAD_REQUEST, "GROUP_400_2","이 그룹에 속하지 않은 멤버입니다."),
    ALREADY_IN_GROUP(BAD_REQUEST, "GROUP_400_3", "이미 그룹에 가입되어 있습니다."),
    GROUP_POST_NOT_FOUND(NOT_FOUND, "GROUP_404_2", "그룹 게시글을 찾을 수 없습니다."),
    GROUP_COMMENT_NOT_FOUND(NOT_FOUND, "GROUP_404_3", "그룹 게시글을 찾을 수 없습니다."),
    NOT_OWNER(FORBIDDEN, "GROUP_403_2", "본인만 삭제할 수 있습니다.");


    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
