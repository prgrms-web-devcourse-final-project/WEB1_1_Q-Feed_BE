package com.wsws.moduleinfra.entity.user;

import com.wsws.moduledomain.user.vo.TargetType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Likes")
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
