package com.wsws.moduleinfra.repo.group;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduledomain.group.repo.GroupMemberRepository;
import com.wsws.moduledomain.group.repo.GroupRepository;
import com.wsws.moduleinfra.entity.chat.ChatMessageEntity;
import com.wsws.moduleinfra.entity.chat.ChatRoomEntity;
import com.wsws.moduleinfra.entity.group.GroupEntity;
import com.wsws.moduleinfra.entity.group.GroupMemberEntity;
import com.wsws.moduleinfra.repo.chat.mapper.ChatMessageMapper;
import com.wsws.moduleinfra.repo.group.mapper.GroupMapper;
import com.wsws.moduleinfra.repo.group.mapper.GroupMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepository {

   private final JpaGroupMemberRepository jpaGroupMemberRepository;
   private final JpaGroupRepository jpaGroupRepository;

    @Override
    public void save(GroupMember groupMember) {
        // ChatMessage -> ChatMessageEntity 변환 후 저장
        GroupEntity groupEntity = jpaGroupRepository.findById(groupMember.getGroupId().getValue())
                .orElseThrow();

        GroupMemberEntity groupMemberEntity = GroupMemberMapper.toEntity(groupMember, groupEntity);
        jpaGroupMemberRepository.save(groupMemberEntity);
    }

    @Override
    public void delete(GroupMember groupMember) {
        // ChatMessage -> ChatMessageEntity 변환 후 저장
        GroupEntity groupEntity = jpaGroupRepository.findById(groupMember.getGroupId().getValue())
                .orElseThrow();

        GroupMemberEntity groupMemberEntity = GroupMemberMapper.toEntity(groupMember, groupEntity);
        jpaGroupMemberRepository.delete(groupMemberEntity);
    }
}
