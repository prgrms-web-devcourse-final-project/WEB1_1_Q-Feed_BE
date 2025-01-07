package com.wsws.moduleapplication.feed.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record AnswerCommentLikedEvent(
        String likerId,
        String userId,
        Long answerCommentId,
        Long answerId,
        FcmType fcmType
) {
}
