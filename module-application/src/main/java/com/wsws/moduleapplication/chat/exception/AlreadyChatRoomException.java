package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class AlreadyChatRoomException extends ApplicationException {
    public static final AlreadyChatRoomException EXCEPTION = new AlreadyChatRoomException();

    private AlreadyChatRoomException() {
        super(ChatServiceErrorCode.ALREADY_CHATROOM);
    }
}
