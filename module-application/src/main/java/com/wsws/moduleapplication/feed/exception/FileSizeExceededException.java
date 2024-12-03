package com.wsws.moduleapplication.feed.exception;

import com.wsws.moduleapplication.chat.exception.ChatServiceErrorCode;
import com.wsws.modulecommon.exception.ApplicationException;

public class FileSizeExceededException extends ApplicationException {
    public FileSizeExceededException(String type, Long maxFileSize) {
        super(FeedServiceErrorCode.FILE_SIZE_EXCEEDED,
                String.format("%s 파일 크기는 %dMB 이하여야 합니다.", type, maxFileSize / (1024 * 1024)));
    }
}