package com.wsws.moduleapplication.feed.dto.answer.edit;

import org.springframework.web.multipart.MultipartFile;

public record AnswerEditServiceRequest(
        Long answerId,
        String userId,
        String content,
        MultipartFile image,
        Boolean visibility
) {
}
