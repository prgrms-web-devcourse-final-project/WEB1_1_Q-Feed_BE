package com.wsws.moduleapplication.socialnetwork.follow.dto;

import java.time.LocalDateTime;

public record FollowServiceResponseDto(String userId, String nickname, String profileImage, LocalDateTime createAt) {
}
