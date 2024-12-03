package com.wsws.moduledomain.group;

import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class Group {
    private GroupId groupId;
    private String groupName;
    private String description;
    private LocalDateTime createdAt;
    private UserId adminId;
    private Long categoryId;
    private String url;
    private boolean isOpen;
   // private List<GroupMember> groupMembers;

    public static Group create(Long groupId,String groupName, String description, LocalDateTime createdAt,
                               String adminId, Long categoryId, String url, boolean isOpen) {
        Group group = new Group();
        group.groupId = GroupId.of(groupId);
        group.groupName = groupName;
        group.description = description;
        group.createdAt = createdAt;
        group.adminId = UserId.of(adminId);
        group.categoryId = categoryId;
        group.url = url;
        group.isOpen = isOpen;
        return group;
    }

    // 그룹 공개 상태 전환
    public void changeVisibility(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void updateGroupInfro(String groupName, String description, String image) {

        if (groupName != null && !groupName.isEmpty()) {
            this.groupName = groupName;
        }
        if (description != null) {
            this.description = description;
        }
        if (image != null) {
            this.url = image;
        }
    }

}
