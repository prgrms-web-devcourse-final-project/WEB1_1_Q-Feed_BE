package com.wsws.moduleinfra.repo.user;

import com.wsws.moduleinfra.entity.user.UserInterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaUserInterestRepository extends JpaRepository<UserInterestEntity, Long> {
    List<UserInterestEntity> findByUserId(String userId);
}
