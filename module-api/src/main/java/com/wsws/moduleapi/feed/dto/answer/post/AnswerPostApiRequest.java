package com.wsws.moduleapi.feed.dto.answer.post;

import com.wsws.moduleapplication.feed.dto.answer.edit.AnswerCreateServiceRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record AnswerPostApiRequest(
        @NotNull(message = "Question Id는 필수 값입니다.")
        Long questionId,
        String content,
        MultipartFile image,
        @NotNull(message = "visibility는 필수 값입니다.")
        Boolean visibility
) {
    public AnswerCreateServiceRequest toServiceDto(String userId) {
        return new AnswerCreateServiceRequest(questionId, content, image, visibility, userId);
    }
}
