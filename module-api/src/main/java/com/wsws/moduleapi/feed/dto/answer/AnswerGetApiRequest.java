package com.wsws.moduleapi.feed.dto.answer;

import jakarta.validation.constraints.NotBlank;

public record AnswerGetApiRequest(

        @NotBlank(message = "질문 ID는 필수입니다.")
        Long questionId
) {
}
