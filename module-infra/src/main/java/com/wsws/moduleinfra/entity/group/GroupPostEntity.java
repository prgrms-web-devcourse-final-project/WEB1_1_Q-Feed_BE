package com.wsws.moduleinfra.entity.group;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "group_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_post_id", nullable = false)
    private Long groupPostId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "url")
    private String url; // 이미지 URL

    @Column(name = "like_count", nullable = false)
    private long likeCount;

//    @OneToMany(mappedBy = "groupPost", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<GroupComment> comments;


        public static GroupPostEntity create(String content, Long groupId, String userId, String url, Long likeCount) {
            GroupPostEntity entity = new GroupPostEntity();
            entity.content = content;
            entity.groupId = groupId;
            entity.userId = userId;
            entity.url = url;
            entity.createAt = LocalDateTime.now();
            entity.likeCount = likeCount;
            return entity;
        }

    public void editEntity(long likeCount) {
            this.likeCount = likeCount;
    }

}
