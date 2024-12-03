package com.wsws.moduledomain.user;

import com.wsws.moduledomain.user.vo.LikeTargetId;
import com.wsws.moduledomain.user.vo.LikeId;
import com.wsws.moduledomain.user.vo.TargetType;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.Getter;

@Getter
public class Like {
    private LikeId likeId;

    private TargetType targetType;

    private LikeTargetId targetId;
    private UserId userId;

    public static Like createLike(Long likeId, TargetType targetType, Long targetId, String userId) {
        Like like = new Like();
        like.likeId = LikeId.of(likeId);
        like.targetType = targetType;
        like.targetId = LikeTargetId.of(targetId);
        like.userId = UserId.of(userId);
        return like;
    }

}
