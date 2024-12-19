package com.wsws.moduledomain.group;

import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupPost {
    private Long groupPostId;
    private String content;
    private LocalDateTime createdAt;
    private GroupId groupId;
    private UserId userId;
    private String url;
    private long likeCount = 0;

    public static GroupPost create(Long groupPostId, Long groupId, String content, String url, String userId, Long likeCount) {
        GroupPost post = new GroupPost();
        post.groupPostId = groupPostId;
        post.groupId = GroupId.of(groupId);
        post.content = content;
        post.url = url;
        post.userId = UserId.of(userId);
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
