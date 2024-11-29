package com.wsws.moduleinfra.repo.follow.dto;


import java.time.LocalDateTime;

public record FollowResponseInfraDto(String userId, String nickname, String profileImage, LocalDateTime createdAt) {
}
