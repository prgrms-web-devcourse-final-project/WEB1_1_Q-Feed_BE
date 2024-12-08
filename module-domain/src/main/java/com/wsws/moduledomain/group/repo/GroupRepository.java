package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.dto.GroupDetailDto;
import com.wsws.moduledomain.group.dto.GroupDto;
import com.wsws.moduledomain.group.dto.GroupMemberDto;
import com.wsws.moduledomain.group.vo.GroupId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    Group save(Group group);

    void deleteById(Long groupId);

    void edit(Group group);

    void changeStatus(Group group);

    Optional<Group> findById(GroupId groupId);

    List<GroupDto> findByCategoryIdWithMemberCount(Long categoryId, LocalDateTime cursor, int size);

    Optional<GroupDetailDto> findGroupWithCategory(Long groupId);

    List<GroupMemberDto> findMembersByGroupId(Long groupId);

    List<GroupDto> findJoinedGroupsByUserId(String userId);
}
