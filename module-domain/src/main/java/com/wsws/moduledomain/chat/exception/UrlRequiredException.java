package com.wsws.moduledomain.chat.exception;

import com.wsws.modulecommon.exception.DomainException;

public class UrlRequiredException extends DomainException {
    public static final UrlRequiredException EXCEPTION = new UrlRequiredException();

    private UrlRequiredException() {
        super(ChatErrorCode.URL_REQUIRED);
    }
}
