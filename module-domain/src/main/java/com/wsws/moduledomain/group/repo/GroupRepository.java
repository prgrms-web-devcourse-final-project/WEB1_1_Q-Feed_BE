package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.Group;

public interface GroupRepository {
    void save(Group group);

    void delete(Group group);
}
