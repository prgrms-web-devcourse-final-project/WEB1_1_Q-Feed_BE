package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.GroupComment;

import java.util.Optional;

public interface GroupCommentRepository {
    void save(GroupComment groupComment);
    Optional<GroupComment> findById(Long groupPostId);
}
