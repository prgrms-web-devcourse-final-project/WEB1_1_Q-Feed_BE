package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.dto.GroupDetailDto;
import com.wsws.moduledomain.group.dto.GroupDto;
import com.wsws.moduledomain.group.dto.GroupMemberDto;
import com.wsws.moduledomain.group.vo.GroupId;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    void save(Group group);

    void delete(Group group);

    Optional<Group> findById(GroupId groupId);

    List<GroupDto> findByCategoryIdWithMemberCount(Long categoryId);

    Optional<GroupDetailDto> findGroupWithCategory(Long groupId);

    List<GroupMemberDto> findMembersByGroupId(Long groupId);
}
