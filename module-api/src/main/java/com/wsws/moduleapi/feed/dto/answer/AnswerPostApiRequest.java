package com.wsws.moduleapi.feed.dto.answer;

import com.wsws.moduleapplication.feed.dto.answer.AnswerCreateServiceRequest;
import org.springframework.web.multipart.MultipartFile;

public record AnswerPostApiRequest(
        Long questionId,
        String content,
        MultipartFile image,
        Boolean visibility
) {
    public AnswerCreateServiceRequest toServiceDto(String userId) {
        return new AnswerCreateServiceRequest(questionId, content, image, visibility, userId);
    }
}
