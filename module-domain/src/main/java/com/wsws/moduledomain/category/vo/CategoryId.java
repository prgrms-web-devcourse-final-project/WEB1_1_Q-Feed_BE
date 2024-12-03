package com.wsws.moduledomain.category.vo;


import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode
public class CategoryId {
    private final Long value;

    private CategoryId(Long value) {
        this.value = value;
    }

    public static CategoryId of(Long value) {
        return new CategoryId(value);
    }
}