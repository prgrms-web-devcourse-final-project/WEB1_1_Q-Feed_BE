package com.wsws.moduleapi.feed.dto.answer;

import com.wsws.moduleapplication.feed.dto.answer.AnswerEditServiceRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record AnswerPatchApiRequest(
        String content,
        MultipartFile image,
        @NotNull
        Boolean visibility
) {
        public AnswerEditServiceRequest toServiceDto(Long answerId) {
                return new AnswerEditServiceRequest(answerId, content, image, visibility);
        }
}
