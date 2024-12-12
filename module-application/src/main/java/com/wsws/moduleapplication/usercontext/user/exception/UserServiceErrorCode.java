package com.wsws.moduleapplication.usercontext.user.exception;

import com.wsws.modulecommon.exception.BaseErrorCode;
import com.wsws.modulecommon.exception.ErrorInfo;
import lombok.RequiredArgsConstructor;

import static com.wsws.modulecommon.constants.ErrorCodeConstants.*;


@RequiredArgsConstructor
public enum UserServiceErrorCode implements BaseErrorCode {
    DUPLICATE_EMAIL(BAD_REQUEST, "USER_APP_400_1", "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(BAD_REQUEST, "USER_APP_400_2", "이미 사용 중인 닉네임입니다."),
    ALREADY_LIKED(BAD_REQUEST, "USER_APP_400_3","이미 좋아요를 누른 글입니다."),
    NOT_LIKED(BAD_REQUEST, "USER_APP_400_4","좋아요를 누른적이 없는 글입니다."),
    USER_NOT_FOUND(NOT_FOUND, "USER_APP_404_1", "사용자를 찾을 수 없습니다."),
    PROFILE_IMAGE_PROCESSING_ERROR(INTERNAL_SERVER, "USER_APP_500_1", "프로필 이미지 처리 중 오류가 발생했습니다."),
    PROFILE_IMAGE_REQUIRED(BAD_REQUEST, "USER_APP_400_5", "프로필 이미지를 업로드해야 합니다."),
    PROFILE_IMAGE_TOO_LARGE(BAD_REQUEST, "USER_APP_400_6", "프로필 이미지 파일 크기는 5MB 이하여야 합니다."),
    UNSUPPORTED_IMAGE_FORMAT(BAD_REQUEST, "USER_APP_400_7", "지원하지 않는 이미지 포맷입니다. JPEG, PNG, GIF만 허용됩니다.");



    private final Integer status;
    private final String errorCode;
    private final String message;

    @Override
    public ErrorInfo getErrorInfo() {
        return ErrorInfo.of(status, errorCode, message);
    }
}
