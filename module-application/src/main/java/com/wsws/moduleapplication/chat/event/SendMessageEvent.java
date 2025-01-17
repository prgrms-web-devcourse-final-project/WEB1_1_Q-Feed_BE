package com.wsws.moduleapplication.chat.event;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record SendMessageEvent (
        String senderId,
        String recipientId,
        FcmType fcmType
) {

}
