package com.wsws.moduleinfra.storage;

import com.wsws.modulecommon.exception.InfraException;

public class FileUploadFailException extends InfraException {
    public static final FileUploadFailException EXCEPTION = new FileUploadFailException();

    private FileUploadFailException() {
        super(S3ErrorCode.FILE_UPLOAD_FAILED);
    }
}
