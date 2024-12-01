package com.wsws.moduleapplication.chat.exception;


import com.wsws.moduleapplication.user.exception.UserServiceErrorCode;
import com.wsws.modulecommon.exception.ApplicationException;

public class FileProcessingException extends ApplicationException {
    public static final FileProcessingException EXCEPTION = new FileProcessingException();

    private FileProcessingException() {
        super(ChatServiceErrorCode.FILE_PROCESSING_ERROR);
    }
}
