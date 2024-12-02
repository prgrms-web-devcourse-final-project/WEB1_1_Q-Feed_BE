package com.wsws.moduleinfra.entity.group;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "group_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupMemberId;

    @Column(nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    public static GroupMemberEntity create(String userId, GroupEntity group) {
        GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
        groupMemberEntity.userId = userId;
        groupMemberEntity.group = group;
        return groupMemberEntity;
    }
}
