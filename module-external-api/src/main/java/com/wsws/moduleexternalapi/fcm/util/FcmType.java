package com.wsws.moduleexternalapi.fcm.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FcmType {
    COMMENT("댓글"),
    LIKE("좋아요"),
    CHAT("채팅"),
    FOLLOW("팔로우"),
    Q_SPACE_LIKE("좋아요"),
    Q_SPACE_COMMENT("댓글");
    private final String type;
}
