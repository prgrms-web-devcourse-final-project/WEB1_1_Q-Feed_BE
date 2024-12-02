package com.wsws.moduledomain.chat.exception;

import com.wsws.modulecommon.exception.DomainException;
import com.wsws.moduledomain.user.exception.InvalidEmailFormatException;
import com.wsws.moduledomain.user.exception.UserErrorCode;

public class InvalidChatFormatException extends DomainException {
    public static final InvalidChatFormatException EXCEPTION = new InvalidChatFormatException();

    private InvalidChatFormatException() {
        super(ChatErrorCode.INVALID_CHAT_FORMAT);
    }
}