package com.wsws.moduledomain.group;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupPost {
    private Long groupPostId;
    private String content;
    private LocalDateTime createdAt;
    private Long groupId;
    private String userId;
    private String url;
    private long likeCount = 0;
    private List<GroupComment> comments;

    public static GroupPost create(Long groupPostId, Long groupId, String content, String url, String userId, Long likeCount) {
        GroupPost post = new GroupPost();
        post.groupPostId = groupPostId;
        post.groupId = groupId;
        post.content = content;
        post.url = url;
        post.userId = userId;
        post.createdAt = LocalDateTime.now();
        post.likeCount = likeCount;
        return post;
    }

    // 좋아요 증가
    public void incrementLike() {
        this.likeCount += 1;
    }

    // 좋아요 감소
    public void decrementLike() {
        if (this.likeCount > 0) {
            this.likeCount -= 1;
        }
    }

}
