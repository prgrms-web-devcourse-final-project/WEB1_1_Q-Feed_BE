package com.wsws.moduleapplication.user.dto;

import com.wsws.moduledomain.user.User;

public record UserProfileResponse(
        String userId,
        String email,
        String nickname,
        String profileImage,
        String description,
        int followerCount,
        int followingCount
) {
    public UserProfileResponse(User user, int followerCount, int followingCount) {
        this(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getNickname().getValue(),
                user.getProfileImage(),
                user.getDescription(),
                followerCount,
                followingCount
        );
    }
}
