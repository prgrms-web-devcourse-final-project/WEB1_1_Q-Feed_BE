package com.wsws.moduleapi.feed.dto.answer.put_patch;

import com.wsws.moduleapplication.feed.dto.answer.edit.AnswerEditServiceRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record AnswerPatchApiRequest(
        String content,
        MultipartFile image,
        @NotNull(message = "visibility는 필수값입니다.")
        Boolean visibility
) {
        public AnswerEditServiceRequest toServiceDto(Long answerId, String userId) {
                return new AnswerEditServiceRequest(answerId, userId, content, image, visibility);
        }
}
