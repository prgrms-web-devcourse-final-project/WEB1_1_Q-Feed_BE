package com.wsws.moduleapplication.usercontext.user.dto;

import com.wsws.moduledomain.usercontext.user.aggregate.User;

public record UserProfileResponse(
        String userId,
        String email,
        String nickname,
        String profileImage,
        String description

) {
    public UserProfileResponse(User user) {
        this(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getNickname().getValue(),
                user.getProfileImage(),
                user.getDescription()
        );
    }
}
