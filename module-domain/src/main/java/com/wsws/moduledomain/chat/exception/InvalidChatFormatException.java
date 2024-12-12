package com.wsws.moduledomain.chat.exception;

import com.wsws.modulecommon.exception.DomainException;

public class InvalidChatFormatException extends DomainException {
    public static final InvalidChatFormatException EXCEPTION = new InvalidChatFormatException();

    private InvalidChatFormatException() {
        super(ChatErrorCode.INVALID_CHAT_FORMAT);
    }
}