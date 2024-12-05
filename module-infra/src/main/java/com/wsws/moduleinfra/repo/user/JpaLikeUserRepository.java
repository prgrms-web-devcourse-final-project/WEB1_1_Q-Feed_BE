package com.wsws.moduleinfra.repo.user;

import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.vo.TargetType;
import com.wsws.moduleinfra.entity.user.LikeEntity;
import com.wsws.moduleinfra.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
