package com.wsws.moduleapplication.usercontext.user.dto;

public record FullUserProfileResponse(
        String userId,
        String nickname,
        String email,
        String profileImageUrl,
        String description,
        long followerCount,
        long followingCount
) {
    public FullUserProfileResponse(UserProfileResponse profile, long followerCount, long followingCount) {
        this(
                profile.userId(),
                profile.nickname(),
                profile.email(),
                profile.profileImage(),
                profile.description(),
                followerCount,
                followingCount
        );
    }
}
