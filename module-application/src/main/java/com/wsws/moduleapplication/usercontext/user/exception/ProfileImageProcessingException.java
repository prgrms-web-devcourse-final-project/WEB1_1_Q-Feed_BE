package com.wsws.moduleapplication.usercontext.user.exception;


import com.wsws.modulecommon.exception.ApplicationException;

public class ProfileImageProcessingException extends ApplicationException {
    public static final ProfileImageProcessingException EXCEPTION = new ProfileImageProcessingException();

    private ProfileImageProcessingException() {
        super(UserServiceErrorCode.PROFILE_IMAGE_PROCESSING_ERROR);
    }
}
