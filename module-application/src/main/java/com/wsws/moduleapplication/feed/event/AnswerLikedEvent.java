package com.wsws.moduleapplication.feed.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record AnswerLikedEvent(
        String senderId,
        String recipientId,
        Long answerId,
        FcmType fcmType
) {
}
