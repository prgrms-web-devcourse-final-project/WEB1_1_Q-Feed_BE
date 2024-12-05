package com.wsws.moduledomain.notification.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@EqualsAndHashCode
@Getter
public class CreatedAt {

    private LocalDateTime createdAt;

    public CreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}
