package com.wsws.moduledomain.feed.like;

import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.Getter;

@Getter
public class Like {
    private LikeId likeId;

    private TargetType targetType;

    private LikeTargetId targetId;
    private UserId userId;

    public static Like create(Long likeId, TargetType targetType, Long targetId, String userId) {
        Like like = new Like();
        like.likeId = LikeId.of(likeId);
        like.targetType = targetType;
        like.targetId = LikeTargetId.of(targetId);
        like.userId = UserId.of(userId);
        return like;
    }

}
