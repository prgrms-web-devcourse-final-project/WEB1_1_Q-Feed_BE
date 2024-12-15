package com.wsws.moduleapi.user.dto;

import com.wsws.moduleapplication.usercontext.user.dto.UserProfileResponse;

public record UserProfileApiResponse(
        String userId,
        String email,
        String nickname,
        String profileImage,
        String description,
        int followerCount,
        int followingCount
) {

    public UserProfileApiResponse(UserProfileResponse serviceResponse, int followerCount, int followingCount) {
        this(
                serviceResponse.userId(),
                serviceResponse.email(),
                serviceResponse.nickname(),
                serviceResponse.profileImage(),
                serviceResponse.description(),
                followerCount,
                followingCount
        );
    }
}
