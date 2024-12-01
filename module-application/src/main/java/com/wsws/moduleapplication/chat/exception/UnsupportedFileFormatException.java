package com.wsws.moduleapplication.chat.exception;

import com.wsws.modulecommon.exception.ApplicationException;

public class UnsupportedFileFormatException extends ApplicationException {
    public UnsupportedFileFormatException(String type) {
        super(ChatServiceErrorCode.UNSUPPORTED_FILE_FORMAT,
                String.format("지원하지 않는 %s 포맷입니다.", type));
    }
}