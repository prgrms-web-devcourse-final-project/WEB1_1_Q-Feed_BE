package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.GroupPost;
import com.wsws.moduledomain.group.dto.GroupPostDto;

import java.util.List;
import java.util.Optional;


public interface GroupPostRepository {
    void save(GroupPost groupPost);
    void delete(Long groupPostId);
//    Optional<GroupPostDetailDto> findById(Long groupPostId);
    List<GroupPostDto> findByGroupId(Long groupId);
    void delete(GroupPost groupPost);
    Optional<Object> findById(Long groupPostId);
}
