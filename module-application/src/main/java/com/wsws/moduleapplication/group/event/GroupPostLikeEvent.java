package com.wsws.moduleapplication.group.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record GroupPostLikeEvent(
        String likerId,
        String userId,
        Long postId,
        FcmType fcmType
) {
}
