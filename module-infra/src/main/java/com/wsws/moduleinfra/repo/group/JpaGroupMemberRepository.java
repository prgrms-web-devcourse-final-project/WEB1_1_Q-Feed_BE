package com.wsws.moduleinfra.repo.group;

import com.wsws.moduleinfra.entity.group.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {
}
