package com.wsws.moduleapplication.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public class FileValidator {

    private static final Map<String, Long> MAX_FILE_SIZES = Map.of(
            "image", 5L * 1024 * 1024, // 이미지 파일 최대 크기: 5MB
            "audio", 10L * 1024 * 1024 // 오디오 파일 최대 크기: 10MB
    );

    private static final Map<String, String[]> ALLOWED_FORMATS = Map.of(
            "image", new String[]{"image/jpeg", "image/png", "image/gif"},
            "audio", new String[]{"audio/mpeg", "audio/wav"}
    );

    public static void validate(MultipartFile file, String type) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일을 업로드해야 합니다.");
        }

        // 최대 크기 확인
        Long maxFileSize = MAX_FILE_SIZES.getOrDefault(type, 0L);
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(type + " 파일 크기는 " + (maxFileSize / (1024 * 1024)) + "MB 이하여야 합니다.");
        }

        // 파일 형식 확인
        String[] allowedFormats = ALLOWED_FORMATS.getOrDefault(type, new String[]{});
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedFormat(contentType, allowedFormats)) {
            throw new IllegalArgumentException("지원하지 않는 " + type + " 포맷입니다.");
        }
    }

    private static boolean isAllowedFormat(String contentType, String[] allowedFormats) {
        for (String format : allowedFormats) {
            if (format.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
}
