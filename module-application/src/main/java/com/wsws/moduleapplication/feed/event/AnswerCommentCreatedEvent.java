package com.wsws.moduleapplication.feed.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record AnswerCommentCreatedEvent(
        String senderId,
        String recipientId,
        Long answerId,
        Long commentId,
        FcmType fcmType
) {
}
