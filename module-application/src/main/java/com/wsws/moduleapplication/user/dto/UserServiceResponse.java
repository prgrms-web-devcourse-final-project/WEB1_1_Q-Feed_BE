package com.wsws.moduleapplication.user.dto;

import com.wsws.moduledomain.user.User;

public record UserServiceResponse(
        String userId,
        String email,
        String nickname,
        String profileImageUrl
) {
    public UserServiceResponse(User user){
        this(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getNickname().getValue(),
                user.getProfileImage()
        );
    }
}
