package com.wsws.moduledomain.group;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GroupPost {
    private Long groupPostId;
    private String content;
    private LocalDateTime createdAt;
    private String groupId;
    private String userId;
    private String url;
    private long likeCount = 0;

    public static GroupPost create( String groupId, String content,String url) {
        GroupPost post = new GroupPost();
        post.groupId = groupId;
        post.content = content;
        post.url = url;
        post.createdAt = LocalDateTime.now();
        post.likeCount = 0;
        return post;
    }

    // 좋아요 증가
    public void incrementLike() {
        this.likeCount++;
    }

    // 좋아요 감소
    public void decrementLike() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
