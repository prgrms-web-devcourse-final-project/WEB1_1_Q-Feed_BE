package com.wsws.moduleapi.dto.user;

import java.util.List;

public record UserResponse(
        String email,
        String nickname,
        String profileImage,
        int followerCount,
        int followingCount,
        List<String> hobbies
) {
}
