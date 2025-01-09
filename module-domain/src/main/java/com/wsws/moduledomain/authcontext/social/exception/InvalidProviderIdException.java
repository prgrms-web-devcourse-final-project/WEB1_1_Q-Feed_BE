package com.wsws.moduledomain.authcontext.social.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidProviderIdException extends DomainException {
    public static final InvalidProviderIdException EXCEPTION = new InvalidProviderIdException();

    private InvalidProviderIdException() {
        super(SocialErrorCode.EMPTY_PROVIDER_ID);
    }
}

