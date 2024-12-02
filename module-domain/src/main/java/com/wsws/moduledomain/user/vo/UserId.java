package com.wsws.moduledomain.user.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
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

    @Column(name = "user_id")
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
