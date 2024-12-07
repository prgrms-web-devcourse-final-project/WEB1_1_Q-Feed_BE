package com.wsws.moduleapi.feed.dto.answer.put_patch;

import com.wsws.moduleapplication.feed.dto.answer.AnswerEditServiceRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record AnswerPatchApiRequest(
        String content,
        MultipartFile image,
        @NotNull(message = "visibility는 필수값입니다.")
        Boolean visibility
) {
        public AnswerEditServiceRequest toServiceDto(Long answerId) {
                return new AnswerEditServiceRequest(answerId, content, image, visibility);
        }
}
