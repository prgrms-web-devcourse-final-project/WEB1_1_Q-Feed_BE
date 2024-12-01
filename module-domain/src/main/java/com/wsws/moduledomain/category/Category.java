package com.wsws.moduledomain.category;

import com.wsws.moduledomain.category.vo.CategoryName;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
public class Category {
    private Long id;
    private CategoryName categoryName;

    public static Category create(CategoryName categoryName) {
        Category category = new Category();
        category.categoryName = categoryName;
        return category;
    }
}

