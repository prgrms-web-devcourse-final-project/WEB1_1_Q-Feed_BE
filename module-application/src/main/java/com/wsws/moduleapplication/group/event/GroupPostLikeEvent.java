package com.wsws.moduleapplication.group.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record GroupPostLikeEvent(
        String senderId,
        String recipientId,
        Long postId,
        FcmType fcmType
) {
}
