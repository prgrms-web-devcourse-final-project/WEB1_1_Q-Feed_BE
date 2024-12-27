package com.wsws.modulesecurity.security;

import com.wsws.moduledomain.usercontext.user.aggregate.User;

public class UserPrincipalMapper {

    public static UserPrincipal fromDomain(User user) {

        String roleName = user.getUserRole().name();

        return new UserPrincipal(user.getId().getValue(), roleName);
    }
}
