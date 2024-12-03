package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.repo.GroupPostRepository;
import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduleinfra.entity.group.GroupPostEntity;
import com.wsws.moduleinfra.repo.group.mapper.GroupPostMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.wsws.moduledomain.group.GroupPost;

@RequiredArgsConstructor
public class GroupPostRepositoryImpl implements GroupPostRepository {

    private final JpaGroupPostRepository jpaGroupPostRepository;

    @Override
    public void save(GroupPost groupPost) {
        GroupPostEntity groupPostEntity = GroupPostMapper.toEntity(groupPost);
        jpaGroupPostRepository.save(groupPostEntity);
    }

    @Override
    public void delete(Long groupPostId) {
        jpaGroupPostRepository.deleteById(groupPostId);
    }

    @Override
    public Optional<GroupPost> findById(Long groupPostId) {
        return jpaGroupPostRepository.findById(groupPostId)
                .map(GroupPostMapper::toDomain);
    }

    @Override
    public List<GroupPost> findByGroupId(String groupId) {
        List<GroupPostEntity> groupPostEntities = jpaGroupPostRepository.findByGroupId(groupId);
        return groupPostEntities.stream()
                .map(GroupPostMapper::toDomain)
                .collect(Collectors.toList());
    }
}
