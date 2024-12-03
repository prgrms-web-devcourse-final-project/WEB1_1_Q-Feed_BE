package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.dto.GroupDetailDto;
import com.wsws.moduledomain.group.dto.GroupDto;
import com.wsws.moduledomain.group.dto.GroupMemberDto;
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
    public List<GroupDto> findByCategoryIdWithMemberCount(Long categoryId) {
        return jpaGroupRepository.findByCategoryIdWithMemberCount(categoryId);
    }

    //카테고리 이름을 얻어 그룹 목록 반환
    @Override
    public Optional<GroupDetailDto> findGroupWithCategory(Long groupId) {
        return jpaGroupRepository.findGroupWithCategory(groupId);

    }

    //user 정보가 포함된 그룹 멤버 반환
    @Override
    public List<GroupMemberDto> findMembersByGroupId(Long groupId) {
        return jpaGroupRepository.findMembersByGroupId(groupId);
    }

}
