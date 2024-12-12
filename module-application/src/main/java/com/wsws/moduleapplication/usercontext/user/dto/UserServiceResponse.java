package com.wsws.moduleapplication.usercontext.user.dto;

import com.wsws.moduledomain.usercontext.user.aggregate.User;

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
