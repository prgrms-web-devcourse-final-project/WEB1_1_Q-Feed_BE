package com.wsws.moduleinfra.repo.category;

import com.wsws.moduledomain.category.vo.CategoryName;
import com.wsws.moduleinfra.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    CategoryEntity findByCategoryName(CategoryName categoryName);

}
