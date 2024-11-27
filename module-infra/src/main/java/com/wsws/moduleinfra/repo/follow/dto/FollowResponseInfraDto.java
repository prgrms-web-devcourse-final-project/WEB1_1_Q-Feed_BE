package com.wsws.moduleinfra.repo.follow.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter

public record FollowResponseInfraDto(String userId, String nickname, String profileImage) {
}
