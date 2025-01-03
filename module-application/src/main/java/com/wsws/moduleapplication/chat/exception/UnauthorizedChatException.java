package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class UnauthorizedChatException extends ApplicationException {
    public static final UnauthorizedChatException EXCEPTION = new UnauthorizedChatException();

    private UnauthorizedChatException() {
        super(ChatServiceErrorCode.UNAUTHORIZED_ACCESS_CHAT);
    }
}
