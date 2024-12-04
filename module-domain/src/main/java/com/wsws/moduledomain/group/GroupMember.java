package com.wsws.moduledomain.group;

import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMember {
    private Long groupMemberId;
    private UserId userId;
    private GroupId groupId;

    public static GroupMember create(Long groupMemberId, String userId, Long groupId) {
        GroupMember groupMember = new GroupMember();
        groupMember.groupMemberId = groupMemberId;
        groupMember.userId = UserId.of(userId);
        groupMember.groupId = GroupId.of(groupId);
        return groupMember;
    }
}
