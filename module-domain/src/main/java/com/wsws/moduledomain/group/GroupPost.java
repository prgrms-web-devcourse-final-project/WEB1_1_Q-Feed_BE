package com.wsws.moduledomain.group;

import com.wsws.moduledomain.group.vo.GroupId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GroupPost {
    private Long groupPostId;
    private String content;
    private LocalDateTime createdAt;
    private GroupId groupId;
    private String userId;
    private String url;
    private long likeCount = 0;

    // 게시글 생성
    public static GroupPost create(String content, GroupId groupId, String url) {
        GroupPost groupPost = new GroupPost();
        groupPost.content = content;
        groupPost.createdAt = LocalDateTime.now();
        groupPost.groupId = groupId;
        groupPost.url = url;
        return groupPost;
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
