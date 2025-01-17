package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class ChatReceiverNotFoundException extends ApplicationException {
    public static final ChatReceiverNotFoundException EXCEPTION = new ChatReceiverNotFoundException();

    private ChatReceiverNotFoundException() {
        super(ChatServiceErrorCode.CHAT_RECEIVER_NOT_FOUND);
    }
}
