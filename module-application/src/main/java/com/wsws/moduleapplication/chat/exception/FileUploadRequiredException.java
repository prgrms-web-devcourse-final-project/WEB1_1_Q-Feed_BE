package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class FileUploadRequiredException extends ApplicationException {
    public static final FileUploadRequiredException EXCEPTION = new FileUploadRequiredException();

    private FileUploadRequiredException() { super(ChatServiceErrorCode.FILE_UPLOAD_REQUIRED);}
}