package com.wsws.moduledomain.feed.like;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class LikeId implements Serializable {

    private Long value;

    private LikeId(Long value) {
        this.value = value;
    }

    public static LikeId of(Long id) {
        return new LikeId(id);
    }
}
