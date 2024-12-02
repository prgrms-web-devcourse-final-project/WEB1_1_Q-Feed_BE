package com.wsws.moduleapplication.follow.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FollowServiceResponseDto(String userId, String nickname, String profileImage, LocalDateTime createAt) {
}
