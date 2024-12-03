package com.wsws.moduledomain.user.repo;

import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;

public interface LikeRepository {
    Like save(Like like, User user);
}
