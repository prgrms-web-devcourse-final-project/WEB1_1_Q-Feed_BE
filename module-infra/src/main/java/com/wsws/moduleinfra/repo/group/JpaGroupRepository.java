package com.wsws.moduleinfra.repo.group;

import com.wsws.moduleinfra.entity.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGroupRepository extends JpaRepository<GroupEntity, Long> {
}
