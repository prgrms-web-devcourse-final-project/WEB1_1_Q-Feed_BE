package com.wsws.moduleapplication.feed.dto.answer;

import org.springframework.web.multipart.MultipartFile;

public record AnswerEditServiceRequest(
        Long answerId,
        String content,
        MultipartFile image,
        Boolean visibility
) {
}
