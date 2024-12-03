package com.wsws.moduleinfra.entity;

import com.wsws.moduledomain.category.vo.CategoryName;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CategoryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;

    public static CategoryEntity create(CategoryName categoryName) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.categoryName = categoryName;
        return categoryEntity;
    }
}
