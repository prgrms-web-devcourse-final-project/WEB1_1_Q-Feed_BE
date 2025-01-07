package com.wsws.moduleapplication.feed.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record AnswerCommentCreatedEvent(
        String commenterId,
        String userId,
        Long answerId,
        Long commentId,
        FcmType fcmType
) {
}
