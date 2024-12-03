package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.repo.GroupRepository;
import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduleinfra.entity.group.GroupEntity;
import com.wsws.moduleinfra.repo.group.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final JpaGroupRepository jpaGroupRepository;

    @Override
    public void save(Group group) {
        GroupEntity groupEntity = GroupMapper.toEntity(group);
        jpaGroupRepository.save(groupEntity);
    }

    @Override
    public void delete(Group group) {
        GroupEntity groupEntity = GroupMapper.toEntity(group);
        jpaGroupRepository.delete(groupEntity);
    }

    @Override
    public Optional<Group> findById(GroupId groupId) {
        return jpaGroupRepository.findById(groupId.getValue())
                .map(GroupMapper::toDomain);
    }

    @Override
    public List<Group> findByCategoryId(Long categoryId) {
        return jpaGroupRepository.findByCategoryId(categoryId).stream()
                .map(GroupMapper::toDomain)
                .toList();
    }

}
