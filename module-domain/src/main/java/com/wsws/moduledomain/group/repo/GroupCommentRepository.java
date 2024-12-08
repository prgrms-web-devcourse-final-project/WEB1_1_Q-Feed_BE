package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.GroupComment;

import java.util.List;
import java.util.Optional;

public interface GroupCommentRepository {
    void save(GroupComment groupComment);
    Optional<GroupComment> findById(Long groupCommentId);
    void deleteById(Long groupCommentId);
    void edit(GroupComment groupComment);
    List<GroupComment> findAllByGroupPostId(Long groupPostId);


}
