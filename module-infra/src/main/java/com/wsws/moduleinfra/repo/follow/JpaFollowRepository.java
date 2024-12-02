package com.wsws.moduleinfra.repo.follow;

import com.wsws.moduledomain.follow.Follow;
import com.wsws.moduledomain.follow.repo.FollowRepository;
import com.wsws.moduleinfra.entity.follow.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaFollowRepository extends JpaRepository<FollowEntity, String> {
    Optional<FollowEntity> findByFollowerIdAndFolloweeId(String followerId, String followeeId);
}
