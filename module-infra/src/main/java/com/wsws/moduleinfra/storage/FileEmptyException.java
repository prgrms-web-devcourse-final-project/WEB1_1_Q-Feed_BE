package com.wsws.moduleinfra.storage;

import com.wsws.modulecommon.exception.InfraException;

public class FileEmptyException extends InfraException {

    public static final FileEmptyException EXCEPTION = new FileEmptyException();

    private FileEmptyException() {
        super(S3ErrorCode.EMPTY_FILE);
    }
}
