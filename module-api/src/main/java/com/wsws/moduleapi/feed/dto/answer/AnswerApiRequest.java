package com.wsws.moduleapi.feed.dto.answer;

import jakarta.validation.constraints.NotBlank;

public record AnswerApiRequest(

        @NotBlank(message = "질문 ID는 필수입니다.")
        Long questionId
) {
}
