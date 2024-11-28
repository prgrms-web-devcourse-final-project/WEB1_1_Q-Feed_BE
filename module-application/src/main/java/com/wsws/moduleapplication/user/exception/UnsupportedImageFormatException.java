package com.wsws.moduleapplication.user.exception;

import com.wsws.moduleapi.exception.ApplicationException;
import com.wsws.moduleapi.exception.DomainException;
import com.wsws.moduledomain.user.exception.UserErrorCode;

public class UnsupportedImageFormatException extends ApplicationException {
    public static final UnsupportedImageFormatException EXCEPTION = new UnsupportedImageFormatException();

    private UnsupportedImageFormatException() {
        super(UserServiceErrorCode.UNSUPPORTED_IMAGE_FORMAT);
    }
}
