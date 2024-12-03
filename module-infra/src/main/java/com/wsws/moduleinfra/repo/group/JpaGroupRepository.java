package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.dto.GroupDto;
import com.wsws.moduleinfra.entity.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaGroupRepository extends JpaRepository<GroupEntity, Long> {

    //카테고리 ID로 그룹 목록 조회
    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupDto(" +
            "g.groupId, g.url, g.groupName, g.description, g.isOpen, g.createdAt, COUNT(gm)) " +
            "FROM GroupEntity g " +
            "LEFT JOIN g.groupMembers gm " +
            "WHERE g.categoryId = :categoryId " +
            "GROUP BY g.groupId")
    List<GroupDto> findByCategoryIdWithMemberCount(@Param("categoryId") Long categoryId);
}
