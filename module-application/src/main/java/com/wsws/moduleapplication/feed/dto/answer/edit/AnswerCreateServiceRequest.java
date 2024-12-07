package com.wsws.moduleapplication.feed.dto.answer.edit;

import org.springframework.web.multipart.MultipartFile;

public record AnswerCreateServiceRequest(
        Long questionId,
        String content,
        MultipartFile image,
        Boolean visibility,
        String userId
) {
}
