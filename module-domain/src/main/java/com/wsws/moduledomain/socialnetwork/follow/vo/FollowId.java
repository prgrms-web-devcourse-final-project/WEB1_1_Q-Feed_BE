package com.wsws.moduledomain.socialnetwork.follow.vo;


import com.wsws.moduledomain.socialnetwork.follow.exception.InvalidFollowException;
import com.wsws.moduledomain.socialnetwork.follow.exception.InvalidFolloweeIdException;
import com.wsws.moduledomain.socialnetwork.follow.exception.InvalidFollowerIdException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class FollowId {
    private final String followerId;
    private final String followeeId;

    private FollowId(String followerId, String followeeId) {

        if(followerId == null || followerId.isEmpty()){
            throw InvalidFollowerIdException.EXCEPTION;
        }

        if(followeeId == null || followeeId.isEmpty()){
            throw InvalidFolloweeIdException.EXCEPTION;
        }

        if(followerId.equals(followeeId)){
            throw InvalidFollowException.EXCEPTION;
        }
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public static FollowId of(String followerId, String followeeId) {
        return new FollowId(followerId, followeeId);
    }
}
