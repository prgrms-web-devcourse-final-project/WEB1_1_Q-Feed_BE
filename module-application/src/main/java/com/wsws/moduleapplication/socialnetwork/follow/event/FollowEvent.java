package com.wsws.moduleapplication.socialnetwork.follow.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record FollowEvent (
        String followerId,
        String followeeId,
        FcmType fcmType
){
}
