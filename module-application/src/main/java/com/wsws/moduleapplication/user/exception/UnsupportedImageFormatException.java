package com.wsws.moduleapplication.user.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class UnsupportedImageFormatException extends ApplicationException {
    public static final UnsupportedImageFormatException EXCEPTION = new UnsupportedImageFormatException();

    private UnsupportedImageFormatException() {
        super(UserServiceErrorCode.UNSUPPORTED_IMAGE_FORMAT);
    }
}
