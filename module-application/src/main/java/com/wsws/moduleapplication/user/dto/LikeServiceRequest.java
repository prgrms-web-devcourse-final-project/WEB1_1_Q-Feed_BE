package com.wsws.moduleapplication.user.dto;


public record LikeServiceRequest(
        String userId,
        String targetType,
        Long targetId
) {
}
