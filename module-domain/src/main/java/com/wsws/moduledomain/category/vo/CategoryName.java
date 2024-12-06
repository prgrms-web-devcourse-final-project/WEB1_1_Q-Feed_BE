package com.wsws.moduledomain.category.vo;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CategoryName {
    TRAVEL("travel"),
    SPORTS("sports"),
    FASHION("fashion"),
    CULTURE("culture"),
    DELICIOUS_RESTAURANT("delicious_restaurant"),
    ETC("etc");

    private final String name;

    CategoryName(String name) {
        this.name = name;
    }

    // String으로 받아서 CategoryName으로 변환하는 메서드
    public static CategoryName findByName(final String name) {
        return Arrays.stream(CategoryName.values())
                .filter(category -> category.name.equalsIgnoreCase(name)) // 대소문자 구분 없이 매핑
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category name: " + name));
    }
}