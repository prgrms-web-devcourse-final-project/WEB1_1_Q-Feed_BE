package com.wsws.moduledomain.authcontext.social.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidProviderException extends DomainException {

    public static final InvalidProviderException EXCEPTION = new InvalidProviderException();

    public InvalidProviderException() {
        super(SocialErrorCode.EMPTY_PROVIDER);
    }
}
