package com.wsws.moduledomain.user.vo;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserRole {
    ROLE_ADMIN("admin"),
    ROLE_USER("user");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    //string으로 받아서 userRole enum타입으로 변환시켜주는 메서드
    public static UserRole findByRole(final String role) {
        return Arrays.stream(UserRole.values())
                .filter(uRole -> uRole.role.equals(role))
                .findFirst()
                .orElse(null);
    }
}
