package com.wsws.moduleinfra.entity.follow;

import com.wsws.moduledomain.follow.Follow;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "follows")
public class FollowEntity {

    @EmbeddedId
    private FollowIdEmbeddable id; // EmbeddedId로 FollowId 매핑

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public FollowEntity(FollowIdEmbeddable id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}