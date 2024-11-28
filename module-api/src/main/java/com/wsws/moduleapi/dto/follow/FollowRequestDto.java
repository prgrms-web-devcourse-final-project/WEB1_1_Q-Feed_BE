package com.wsws.moduleapi.dto.follow;

import jakarta.validation.constraints.NotBlank;

public record FollowRequestDto(
        @NotBlank(message = "팔로워 ID는 필수입니다.")
        String followerId,
        @NotBlank(message = "팔로이 ID는 필수입니다.")
        String followeeId
) {
}
