package com.wsws.moduleinfra.repo.group.mapper;

import com.wsws.moduledomain.group.Group;
import com.wsws.moduledomain.group.GroupMember;
import com.wsws.moduleinfra.entity.group.GroupEntity;
import com.wsws.moduleinfra.entity.group.GroupMemberEntity;

public class GroupMapper {
    // Entity -> Domain 변환
    public static Group toDomain(GroupEntity entity) {
        return Group.create(
                entity.getGroupId(),
                entity.getGroupName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getAdminId(),
                entity.getCategoryId(),
                entity.getUrl(),
                entity.getIsOpen()
        );
    }

    // Domain -> Entity 변환
    public static GroupEntity toEntity(Group group) {
        return GroupEntity.create(
                group.getGroupName(),
                group.getDescription(),
                group.getCreatedAt(),
                group.getAdminId().getValue(), // UserId -> String 변환
                group.getCategoryId().getValue(),
                group.getUrl(),
                group.isOpen()
        );
    }

}
