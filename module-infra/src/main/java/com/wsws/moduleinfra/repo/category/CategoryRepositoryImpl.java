package com.wsws.moduleinfra.repo.category;

import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduleinfra.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category findByCategoryName(CategoryName categoryName) {
        CategoryEntity entity = categoryJpaRepository.findByCategoryName(categoryName);
        return toDomain(entity);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryJpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Category findById(CategoryId categoryId) {
        Optional<CategoryEntity> entity = categoryJpaRepository.findById(categoryId.getValue());
        return entity.map(this::toDomain).orElseThrow(() ->
                new IllegalArgumentException("Category not found with ID: " + categoryId));
    }


    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity savedEntity = categoryJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    private Category toDomain(CategoryEntity entity) {
        return Category.create(CategoryId.of(entity.getId()), entity.getCategoryName());
    }

    private CategoryEntity toEntity(Category category) {
        return CategoryEntity.create(category.getCategoryName());
    }
}
