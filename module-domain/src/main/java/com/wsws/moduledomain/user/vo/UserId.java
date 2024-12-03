package com.wsws.moduledomain.user.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class UserId implements Serializable {

    private String value;

    public UserId(String value) {
        this.value = value;
    }

    public static UserId create() {
        return new UserId(UUID.randomUUID().toString());
    }

    public static UserId of(String uuid) {
        return new UserId(uuid);
    }
}
