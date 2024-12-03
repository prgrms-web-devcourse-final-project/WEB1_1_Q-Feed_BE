package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduledomain.group.dto.GroupMemberDetailDto;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository {
    void save(GroupMember groupMember);

    void delete(GroupMember groupMember);

    Optional<GroupMember> findByUserIdAndGroupId(String userId, Long groupId);

    boolean existsByUserIdAndGroupId(String userId, Long groupId);

    List<GroupMemberDetailDto> findMembersByGroupId(Long groupId);
}