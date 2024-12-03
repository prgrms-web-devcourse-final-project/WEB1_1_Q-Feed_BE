package com.wsws.moduledomain.category.repo;

import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.category.vo.CategoryName;

import java.util.List;

public interface CategoryRepository {
    /**
     * 카테고리 이름으로 카테고리 받아오기
     */
    Category findByCategoryName(CategoryName categoryName);

    /**
     * 카테고리 ID로 카테고리 받아오기
     */
    Category findById(CategoryId categoryId);

    /**
     * 카테고리 전부 받아오기
     */
    List<Category> findAllCategories();

    /**
     * 카테고리 저장하기
     */
    Category save(Category category);
}
