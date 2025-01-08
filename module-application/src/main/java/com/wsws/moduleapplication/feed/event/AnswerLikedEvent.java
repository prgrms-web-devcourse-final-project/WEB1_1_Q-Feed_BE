package com.wsws.moduleapplication.feed.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record AnswerLikedEvent(
        String likerId,
        String userId,
        Long answerId,
        FcmType fcmType
) {
}
