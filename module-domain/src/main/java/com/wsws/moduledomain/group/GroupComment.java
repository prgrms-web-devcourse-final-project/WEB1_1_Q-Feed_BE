package com.wsws.moduledomain.group;


import com.wsws.moduledomain.user.vo.UserId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class GroupComment {
    private Long groupCommentId;
    private String content;
    private LocalDateTime createdAt;
    private UserId userId;
    private long likeCount;
    private Long groupPostId;

    private GroupComment(Long groupCommentId, String content, LocalDateTime createdAt, String userId, Long likeCount) {
        this.groupCommentId = groupCommentId;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = UserId.of(userId);
        this.likeCount = likeCount;
    }


    //   생성 메서드
    public static GroupComment create(Long id, String content, LocalDateTime createdAt, String userId, Long likeCount) {
        return new GroupComment(id, content, createdAt, userId, likeCount);
    }

    public Long getGroupPostId() {
        return this.groupPostId;
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
