package com.wsws.moduleapplication.group.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record GroupCommentLikedEvent (
        String senderId,
        String recipientId,
        Long commentId,
        Long postId,
        FcmType fcmType
){
}
