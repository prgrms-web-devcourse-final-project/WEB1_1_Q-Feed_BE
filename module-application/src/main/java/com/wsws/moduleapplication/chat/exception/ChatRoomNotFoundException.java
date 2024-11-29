package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class ChatRoomNotFoundException extends ApplicationException {
    public static final ChatRoomNotFoundException EXCEPTION = new ChatRoomNotFoundException();

    private ChatRoomNotFoundException() {
        super(ChatServiceErrorCode.CHATROOM_NOT_FOUND);
    }
}
