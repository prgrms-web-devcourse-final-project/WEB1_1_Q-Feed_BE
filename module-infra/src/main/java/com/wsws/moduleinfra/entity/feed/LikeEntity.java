package com.wsws.moduleinfra.entity.feed;

import com.wsws.moduledomain.feed.like.TargetType;
import com.wsws.moduleinfra.usercontext.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        name = "Likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"targetId", "user_id"})
        }
)
@Getter
public class LikeEntity {
    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    public static LikeEntity create(TargetType targetType, Long targetId, UserEntity userEntity) {
        LikeEntity likeEntity = new LikeEntity();
        likeEntity.targetType = targetType;
        likeEntity.targetId = targetId;
        likeEntity.userEntity = userEntity;
        return likeEntity;
    }

    /* 연관관계 편의 메서드 */
    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}
