package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.vo.GroupId;

import java.util.List;
import java.util.Optional;

public interface GroupPostRepository {
    void save(GroupPost groupPost);
    void delete(Long groupPostId);
    Optional<GroupPost> findById(Long groupPostId);
    List<GroupPost> findByGroupId(GroupId groupId);
}
