package com.wsws.moduleinfra.repo.user;

import com.wsws.moduledomain.user.User;
import com.wsws.moduleinfra.entity.user.LikeEntity;
import com.wsws.moduleinfra.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLikeUserRepository extends JpaRepository<LikeEntity, Long> {
    boolean existsByTargetIdAndUserEntity(Long targetId, UserEntity user);
}
