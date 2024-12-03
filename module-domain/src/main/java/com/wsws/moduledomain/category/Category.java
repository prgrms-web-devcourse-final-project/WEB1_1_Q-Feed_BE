package com.wsws.moduledomain.category;

import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.category.vo.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class Category {
    private final CategoryId id;
    private final CategoryName categoryName;

    private Category(CategoryId id, CategoryName categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public static Category create(CategoryId id, CategoryName categoryName) {
        return new Category(id, categoryName);
    }
}
