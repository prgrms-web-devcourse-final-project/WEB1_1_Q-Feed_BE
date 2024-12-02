package com.wsws.moduledomain.category;

import com.wsws.moduledomain.category.vo.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Long id;
    private CategoryName categoryName;

    public static Category create(CategoryName categoryName) {
        Category category = new Category();
        category.categoryName = categoryName;
        return category;
    }
}

