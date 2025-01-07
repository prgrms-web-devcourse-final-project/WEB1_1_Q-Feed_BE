package com.wsws.moduleapplication.group.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record GroupCommentLikedEvent (
        String likerId,
        String userId,
        Long commentId,
        Long postId,
        FcmType fcmType
){
}
