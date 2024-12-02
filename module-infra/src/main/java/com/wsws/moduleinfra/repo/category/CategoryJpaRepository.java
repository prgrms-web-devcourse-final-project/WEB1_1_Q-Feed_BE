package com.wsws.moduleinfra.repo.category;

import com.wsws.moduledomain.category.Category;
import com.wsws.moduledomain.category.repo.CategoryRepository;
import com.wsws.moduleinfra.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long>, CategoryRepository {

    @Override
    @Query("select new com.wsws.moduledomain.category.Category(c.id, c.categoryName) from CategoryEntity c")
    List<Category> findAllCategories();
}
