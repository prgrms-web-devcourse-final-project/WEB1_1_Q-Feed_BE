package com.wsws.moduleinfra.repo.user;

import com.wsws.moduleinfra.entity.user.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLikeUserRepository extends JpaRepository<LikeEntity, Long> {
}
