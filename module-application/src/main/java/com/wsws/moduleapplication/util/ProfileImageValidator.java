package com.wsws.moduleapplication.util;

import org.springframework.web.multipart.MultipartFile;

public class ProfileImageValidator {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_FORMATS = {"image/jpeg", "image/png", "image/gif"};

    public static void validate(MultipartFile profileImageFile) {
        if (profileImageFile == null || profileImageFile.isEmpty()) {
            throw new IllegalArgumentException("프로필 이미지를 업로드해야 합니다.");
        }

        if (profileImageFile.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("이미지 파일 크기는 5MB 이하여야 합니다.");
        }

        String contentType = profileImageFile.getContentType();
        if (contentType == null || !isAllowedFormat(contentType)) {
            throw new IllegalArgumentException("지원하지 않는 이미지 포맷입니다. JPEG, PNG, GIF만 허용됩니다.");
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
