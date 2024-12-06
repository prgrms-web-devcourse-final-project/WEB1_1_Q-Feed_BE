package com.wsws.moduleapplication.auth.exception;

import com.wsws.modulecommon.exception.ApplicationException;


public class ProviderIdConflictException extends ApplicationException {
    public static final ProviderIdConflictException EXCEPTION = new ProviderIdConflictException();

    private ProviderIdConflictException() {
        super(AuthServiceErrorCode.PROVIDER_ID_CONFLICT);
    }
}
