package com.wsws.moduledomain.group;


import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class GroupComment {
    private Long groupCommentId;
    private String content;
    private LocalDateTime createdAt;
    private UserId userId;
    private long likeCount;
    private Long groupPostId;


    public static GroupComment create(Long id, String content, LocalDateTime createdAt, String userId, Long likeCount) {
       GroupComment comment = new GroupComment();
       comment.groupCommentId = id;
       comment.content = content;
       comment.createdAt = createdAt;
       comment.userId = UserId.of(userId);
       comment.likeCount = likeCount;
       return comment;
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
