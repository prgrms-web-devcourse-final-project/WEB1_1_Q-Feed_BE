package com.wsws.moduleapi.dto.user;

import com.wsws.moduleapplication.dto.user.UserProfileResponse;

public record UserProfileApiResponse(
        String userId,
        String email,
        String nickname,
        String profileImage,
        String description,
        int followerCount,
        int followingCount
) {

    public UserProfileApiResponse(UserProfileResponse serviceResponse) {
        this(
                serviceResponse.userId(),
                serviceResponse.email(),
                serviceResponse.nickname(),
                serviceResponse.profileImage(),
                serviceResponse.description(),
                serviceResponse.followerCount(),
                serviceResponse.followingCount()
        );
    }
}
