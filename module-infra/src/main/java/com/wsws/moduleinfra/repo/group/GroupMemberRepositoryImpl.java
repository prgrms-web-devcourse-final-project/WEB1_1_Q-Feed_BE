package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduledomain.group.dto.GroupMemberDetailDto;
import com.wsws.moduledomain.group.repo.GroupMemberRepository;
import com.wsws.moduleinfra.entity.group.GroupEntity;
import com.wsws.moduleinfra.entity.group.GroupMemberEntity;
import com.wsws.moduleinfra.repo.group.mapper.GroupMapper;
import com.wsws.moduleinfra.repo.group.mapper.GroupMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepository {

   private final JpaGroupMemberRepository jpaGroupMemberRepository;
   private final JpaGroupRepository jpaGroupRepository;

    @Override
    public void save(GroupMember groupMember) {
        GroupEntity groupEntity = jpaGroupRepository.findById(groupMember.getGroupId().getValue())
                .orElseThrow();

        GroupMemberEntity groupMemberEntity = GroupMemberMapper.toEntity(groupMember, groupEntity);
        jpaGroupMemberRepository.save(groupMemberEntity);
    }

    @Override
    public void delete(GroupMember groupMember) {
        GroupEntity groupEntity = jpaGroupRepository.findById(groupMember.getGroupId().getValue())
                .orElseThrow();

        GroupMemberEntity groupMemberEntity = GroupMemberMapper.toEntity(groupMember, groupEntity);
        jpaGroupMemberRepository.delete(groupMemberEntity);
    }

    @Override
    public Optional<GroupMember> findById(Long memberId) {
        return jpaGroupMemberRepository.findById(memberId)
                .map(GroupMemberMapper::toDomain);
    }

    @Override
    public Optional<GroupMember> findByUserIdAndGroupId(String userId, Long groupId) {
        return jpaGroupMemberRepository.findByUserIdAndGroupMemberId(userId, groupId)
                .map(GroupMemberMapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndGroupId(String userId, Long groupId) {
        return jpaGroupMemberRepository.existsByUserIdAndGroupMemberId(userId, groupId);
    }

    @Override
    public List<GroupMemberDetailDto> findMembersByGroupId(Long groupId) {
        return jpaGroupMemberRepository.findMembersByGroupId(groupId);
    }
}
