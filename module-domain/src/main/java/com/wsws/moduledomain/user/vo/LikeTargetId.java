package com.wsws.moduledomain.user.vo;

import com.wsws.moduledomain.feed.answer.vo.AnswerId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class LikeTargetId {
    private Long value;

    protected LikeTargetId(Long value) {
        this.value = value;
    }

    public static LikeTargetId of(Long id) {
        return new LikeTargetId(id);
    }
}

