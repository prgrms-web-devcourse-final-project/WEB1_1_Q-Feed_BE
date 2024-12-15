package com.wsws.moduleinfra.like;

import com.wsws.moduledomain.feed.like.TargetType;
import com.wsws.moduleinfra.entity.feed.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaLikeUserRepository extends JpaRepository<LikeEntity, Long> {

    /**
     * 특정 targetId와 userId를 가지는 LikeEntity가 있는지
     */
    @Query("SELECT COUNT(l) > 0 FROM LikeEntity l WHERE l.targetId = :targetId AND l.userEntity.id = :userId AND l.targetType = :targetType")
    boolean existsByTargetIdAndUserIdAndTargetType(Long targetId, String userId, TargetType targetType);

    @Modifying
    @Query("DELETE FROM LikeEntity l WHERE l.targetId = :targetId AND l.userEntity.id = :userId")
    void deleteByTargetIdAndUserId(Long targetId, String userId);

    /**
     * 특정 사용자가 누른 글
     */
    @Query("SELECT l FROM LikeEntity l WHERE l.userEntity.id = :userId")
    List<LikeEntity> findByUserId(String userId);

    @Override
    void flush();
}
