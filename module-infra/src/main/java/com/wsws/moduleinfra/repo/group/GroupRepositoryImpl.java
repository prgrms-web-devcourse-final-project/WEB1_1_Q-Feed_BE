package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.dto.GroupDetailDto;
import com.wsws.moduledomain.group.dto.GroupDto;
import com.wsws.moduledomain.group.dto.GroupMemberDto;
import com.wsws.moduledomain.group.repo.GroupRepository;
import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
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
    public Group save(Group group) {
        GroupEntity groupEntity = GroupMapper.toEntity(group);
        jpaGroupRepository.save(groupEntity);
        return group;
    }

    @Override
    public void deleteById(Long groupId) {
        //GroupEntity groupEntity = GroupMapper.toEntity(group);
        jpaGroupRepository.deleteById(groupId);
    }

    @Override
    public void edit(Group group) {
        GroupEntity groupEntity = jpaGroupRepository.findById(group.getGroupId().getValue())
                .orElseThrow(RuntimeException::new);
        groupEntity.editEntity(group.getGroupName(), group.getDescription(), group.getUrl());
    }

    @Override
    public void changeStatus(Group group) {
        GroupEntity groupEntity = jpaGroupRepository.findById(group.getGroupId().getValue())
                .orElseThrow(RuntimeException::new);
        groupEntity.changeStatus(group.isOpen());
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
