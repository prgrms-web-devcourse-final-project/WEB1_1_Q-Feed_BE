package com.wsws.moduleapplication.feed.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record AnswerCommentLikedEvent(
        String senderId,
        String recipientId,
        Long answerCommentId,
        Long answerId,
        FcmType fcmType
) {
}
