package com.wsws.moduleinfra.repo.group;

import com.wsws.moduleinfra.entity.group.GroupCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGroupCommentRepository extends JpaRepository<GroupCommentEntity, Long> {
}
