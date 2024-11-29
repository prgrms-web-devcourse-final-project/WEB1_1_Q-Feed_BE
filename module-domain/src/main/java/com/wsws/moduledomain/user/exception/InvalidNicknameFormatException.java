package com.wsws.moduledomain.user.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidNicknameFormatException extends DomainException {
    public static final InvalidNicknameFormatException EXCEPTION = new InvalidNicknameFormatException();

    private InvalidNicknameFormatException() {
        super(UserErrorCode.INVALID_NICKNAME_FORMAT);
    }

}

