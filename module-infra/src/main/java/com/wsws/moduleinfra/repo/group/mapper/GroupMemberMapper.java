package com.wsws.moduleinfra.repo.group.mapper;

import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduleinfra.entity.group.GroupEntity;
import com.wsws.moduleinfra.entity.group.GroupMemberEntity;

public class GroupMemberMapper {
    // Entity -> Domain 변환
    public static GroupMember toDomain(GroupMemberEntity entity) {
        return GroupMember.create(
                entity.getGroupMemberId(),
                entity.getUserId(),
                entity.getGroup().getGroupId() // GroupEntity -> GroupId 변환
        );
    }

    // Domain -> Entity 변환
    public static GroupMemberEntity toEntity(GroupMember groupMember) {
        return GroupMemberEntity.create(
                groupMember.getUserId().getValue(), // UserId -> String 변환
                null
        );
    }
}
