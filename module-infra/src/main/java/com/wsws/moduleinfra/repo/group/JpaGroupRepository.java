package com.wsws.moduleinfra.repo.group;

import com.wsws.moduleinfra.entity.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaGroupRepository extends JpaRepository<GroupEntity, Long> {

    List<GroupEntity> findByCategoryId(Long categoryId);
}
