package com.wsws.moduleinfra.entity.group;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "group_comments")
public class GroupCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_comment_id", nullable = false)
    private Long groupCommentId;

    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_post_id", nullable = false)
    private GroupPostEntity groupPost;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "like_count", nullable = false)
    private Long likeCount;


    // 생성 메서드
    public static GroupCommentEntity create(String content, LocalDateTime createdAt,String userId, Long likeCount, GroupPostEntity groupPost) {
        GroupCommentEntity entity = new GroupCommentEntity();
        entity.content = content;
        entity.createdAt = createdAt;
        entity.userId = userId;
        entity.likeCount = likeCount;
        entity.groupPost = groupPost;
        return entity;
    }

    // 연관 관계 설정 메서드 (편의 메서드)
    public void setGroupPost(GroupPostEntity groupPost) {
        this.groupPost = groupPost;
    }

    public void editEntity(long likeCount) {
        this.likeCount = likeCount;
    }

}