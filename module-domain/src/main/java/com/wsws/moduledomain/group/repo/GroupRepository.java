package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.vo.GroupId;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    void save(Group group);

    void delete(Group group);

    Optional<Group> findById(GroupId groupId);

    List<Group> findByCategoryId(Long categoryId);
}
