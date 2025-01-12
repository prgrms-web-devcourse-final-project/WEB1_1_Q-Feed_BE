package com.wsws.moduleexternalapi.fcm.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FcmType {
    FOLLOW,
    ANSWER_COMMENT,
    ANSWER_LIKE,
    COMMENT_LIKE,
    Q_SPACE_POST_COMMENT,
    Q_SPACE_POST_LIKE,
    Q_SPACE_COMMENT_LIKE

}
