package com.wsws.moduleinfra.socialnetworkcontext.follow.repository;

import com.wsws.moduleinfra.socialnetworkcontext.follow.entity.FollowEntity;
import com.wsws.moduleinfra.socialnetworkcontext.follow.entity.FollowIdEmbeddable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaFollowRepository extends JpaRepository<FollowEntity, FollowIdEmbeddable> {
    Optional<FollowEntity> findById_FollowerIdAndId_FolloweeId(String followerId, String followeeId);
    List<FollowEntity> findById_FollowerId(String followerId);
}
