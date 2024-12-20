package com.wsws.moduleexternalapi.fcm.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FcmType {
    FOLLOW("팔로우"),
    CHAT("채팅"),
    ANSWER_COMMENT("댓글"),
    ANSWER_LIKE("좋아요"),
    COMMENT_LIKE("좋아요"),
    Q_SPACE_POST_COMMENT("댓글"),
    Q_SPACE_POST_LIKE("좋아요"),
    Q_SPACE_COMMENT_LIKE("좋아요");
    private final String type;
}
