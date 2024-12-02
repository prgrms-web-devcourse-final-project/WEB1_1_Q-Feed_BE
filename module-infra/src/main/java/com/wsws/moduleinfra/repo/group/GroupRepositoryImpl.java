package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.repo.GroupRepository;
import com.wsws.moduleinfra.entity.group.GroupEntity;
import com.wsws.moduleinfra.repo.group.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
