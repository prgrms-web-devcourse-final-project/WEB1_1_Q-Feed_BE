package com.wsws.moduleinfra.socialnetworkcontext.interest.repo;

import com.wsws.moduleinfra.socialnetworkcontext.interest.entity.UserInterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaUserInterestRepository extends JpaRepository<UserInterestEntity, Long> {
    List<UserInterestEntity> findByUserId(String userId);
}
