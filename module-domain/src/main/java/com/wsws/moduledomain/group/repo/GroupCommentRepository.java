package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.GroupComment;
import com.wsws.moduledomain.group.dto.GroupCommentDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GroupCommentRepository {
    void save(GroupComment groupComment);
    Optional<GroupComment> findById(Long groupCommentId);
    void deleteById(Long groupCommentId);
    void edit(GroupComment groupComment);

   List<GroupCommentDto> findByGroupPostId(Long groupPostId);
}
