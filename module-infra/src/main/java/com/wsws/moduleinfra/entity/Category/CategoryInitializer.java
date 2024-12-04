package com.wsws.moduleinfra.entity.Category;

import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduleinfra.entity.CategoryEntity;
import com.wsws.moduleinfra.repo.category.CategoryJpaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryInitializer {
    private final CategoryJpaRepository categoryRepository;

    @PostConstruct
    public void initializeCategories() {
        for (CategoryName categoryName : CategoryName.values()) {
            if (!categoryRepository.existsByCategoryName(categoryName)) {
                CategoryEntity categoryEntity = CategoryEntity.create(categoryName);
                categoryRepository.save(categoryEntity);
            }
        }
    }
}