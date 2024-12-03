package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.dto.GroupMemberDetailDto;
import com.wsws.moduleinfra.entity.group.GroupMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaGroupMemberRepository extends JpaRepository<GroupMemberEntity, Long> {

    Optional<GroupMemberEntity> findByUserIdAndGroupMemberId(String userId, Long groupId);

    boolean existsByUserIdAndGroupMemberId(String userId, Long groupId);

    @Query(
            "SELECT new com.wsws.moduledomain.group.dto.GroupMemberDetailDto(" +
            "gm.groupMemberId,u.nickname,u.profileImage,u.description) " +
            "FROM GroupMemberEntity gm " +
            "JOIN UserEntity u ON gm.userId = u.id " +
            "WHERE gm.group.groupId = :groupId")
    List<GroupMemberDetailDto> findMembersByGroupId(@Param("groupId") Long groupId);
}
