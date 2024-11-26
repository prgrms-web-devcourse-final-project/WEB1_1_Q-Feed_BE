package com.wsws.moduleinfra.repo.follow.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowResponseInfraDto {
    private final String userId;
    private final String nickname;
    private final String profileImage;


}
