package com.wsws.moduleinfra.entity.group;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String adminId;

    @Column(nullable = false)
    private Long categoryId;

    private String url;

    @Column(nullable = false)
    private Boolean isOpen;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMemberEntity> groupMembers;

    public static GroupEntity create(String groupName, String description, LocalDateTime createdAt, String adminId,
                                     Long categoryId,String url,Boolean isOpen) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.groupName = groupName;
        groupEntity.description = description;
        groupEntity.createdAt = createdAt;
        groupEntity.adminId = adminId;
        groupEntity.categoryId = categoryId;
        groupEntity.url = url;
        groupEntity.isOpen = isOpen;
        return groupEntity;
    }

    public void editEntity(String groupName, String description, String image){
        this.groupName = groupName;
        this.description = description;
        this.url = image;
    }

    public void changeStatus(boolean isOpen) {
        this.isOpen = isOpen;
    }


}
