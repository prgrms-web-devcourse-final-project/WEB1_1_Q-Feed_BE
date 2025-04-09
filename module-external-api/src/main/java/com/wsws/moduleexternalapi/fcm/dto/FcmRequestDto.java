package com.wsws.moduleexternalapi.fcm.dto;

import com.wsws.moduleexternalapi.fcm.util.FcmType;

public record FcmRequestDto(
        String recipient,
        FcmType type,
        String sender

) {}
