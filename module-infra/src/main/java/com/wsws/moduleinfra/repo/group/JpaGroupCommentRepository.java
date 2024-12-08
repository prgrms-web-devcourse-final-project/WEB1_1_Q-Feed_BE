package com.wsws.moduleinfra.repo.group;

import com.wsws.moduleinfra.entity.group.GroupCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaGroupCommentRepository extends JpaRepository<GroupCommentEntity, Long> {
    List<GroupCommentEntity> findAllByGroupPost_GroupPostId(Long groupPostId);

}
