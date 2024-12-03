package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.group.GroupPost;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupPostRepository {
    void save(GroupPost groupPost);
    void delete(Long groupPostId);
    Optional<GroupPost> findById(Long groupPostId);
    List<GroupPost> findByGroupId(String groupId);
}
