package com.wsws.moduleapplication.util;

import com.wsws.moduleapplication.usercontext.user.exception.ProfileImageRequiredException;
import com.wsws.moduleapplication.usercontext.user.exception.ProfileImageTooLargeException;
import com.wsws.moduleapplication.usercontext.user.exception.UnsupportedImageFormatException;
import org.springframework.web.multipart.MultipartFile;

public class FeedImageValidator {


    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_FORMATS = {"image/jpeg", "image/png", "image/gif"};

    public static void validate(MultipartFile feedImage) {
        if (feedImage == null || feedImage.isEmpty()) {
            throw ProfileImageRequiredException.EXCEPTION;
        }

        if (feedImage.getSize() > MAX_FILE_SIZE) {
            throw ProfileImageTooLargeException.EXCEPTION;
        }

        String contentType = feedImage.getContentType();
        if (contentType == null || !isAllowedFormat(contentType)) {
            throw UnsupportedImageFormatException.EXCEPTION;
        }
    }

    private static boolean isAllowedFormat(String contentType) {
        for (String format : ALLOWED_FORMATS) {
            if (format.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
}
