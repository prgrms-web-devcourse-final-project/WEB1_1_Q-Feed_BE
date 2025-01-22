package com.wsws.moduleapplication.feed.dto.answer.edit;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record AnswerCreateServiceRequest(
        Long questionId,
        String content,
        MultipartFile image,
        Boolean visibility,
        String userId
) {
}
