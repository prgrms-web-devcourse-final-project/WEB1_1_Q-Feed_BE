package com.wsws.moduleapi.feed.dto.answer.put_patch;

import jakarta.validation.constraints.NotNull;

public record AnswerVisibilityPatchRequest(
        @NotNull(message = "visibility는 필수값입니다.")
        Boolean visibility
) {
}
