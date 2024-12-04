package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.dto.GroupDetailDto;
import com.wsws.moduledomain.group.dto.GroupDto;
import com.wsws.moduledomain.group.dto.GroupMemberDto;
import com.wsws.moduleinfra.entity.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaGroupRepository extends JpaRepository<GroupEntity, Long> {

    //카테고리 ID로 그룹 목록 조회
    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupDto(" +
            "g.groupId, g.url, g.groupName, g.description, g.isOpen, g.createdAt, COUNT(gm)) " +
            "FROM GroupEntity g " +
            "LEFT JOIN g.groupMembers gm " +
            "WHERE g.categoryId = :categoryId " +
            "GROUP BY g.groupId")
    List<GroupDto> findByCategoryIdWithMemberCount(@Param("categoryId") Long categoryId);

    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupDetailDto(" +
            "g.groupId,c.categoryName,g.url,g.groupName,g.description,g.adminId,g.createdAt) " +
            "FROM GroupEntity g " +
            "JOIN CategoryEntity c ON c.id = g.categoryId " +
            "WHERE g.groupId = :groupId")
    Optional<GroupDetailDto> findGroupWithCategory(Long groupId);


    // 그룹 멤버 ID로 유저 정보 가져오기
    @Query("SELECT new com.wsws.moduledomain.group.dto.GroupMemberDto(" +
            "gm.groupMemberId, gm.userId, u.nickname, u.profileImage) " +
            "FROM GroupMemberEntity gm " +
            "JOIN UserEntity u ON u.id = gm.userId " +
            "WHERE gm.group.groupId = :groupId")
    List<GroupMemberDto> findMembersByGroupId(Long groupId);
}
