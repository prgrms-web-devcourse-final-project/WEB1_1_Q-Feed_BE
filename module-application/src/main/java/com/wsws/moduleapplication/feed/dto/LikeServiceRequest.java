package com.wsws.moduleapplication.feed.dto;


public record LikeServiceRequest(
        String userId,
        String targetType,
        Long targetId
) {
}
