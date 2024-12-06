package com.wsws.moduleinfra.storage;

import com.wsws.modulecommon.exception.InfraException;


public class InvalidFileUploadException extends InfraException {

    public static final InvalidFileUploadException EXCEPTION = new InvalidFileUploadException();

    private InvalidFileUploadException() {
        super(S3ErrorCode.FILE_UPLOAD_FAILED);
    }
}
