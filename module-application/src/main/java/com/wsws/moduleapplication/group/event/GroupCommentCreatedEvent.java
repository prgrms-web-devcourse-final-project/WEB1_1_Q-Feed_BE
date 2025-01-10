package com.wsws.moduleapplication.group.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record GroupCommentCreatedEvent (
        String senderId,
        String recipientId,
        Long postId,
        Long commentId,
        FcmType fcmType
){
}
