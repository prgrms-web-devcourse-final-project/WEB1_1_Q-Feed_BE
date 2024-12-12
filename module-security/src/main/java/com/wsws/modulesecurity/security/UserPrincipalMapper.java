package com.wsws.modulesecurity.security;

import com.wsws.moduledomain.usercontext.user.aggregate.User;

public class UserPrincipalMapper {

    public static UserPrincipal fromDomain(User user) {
        return new UserPrincipal(user.getId().getValue());
    }
}
