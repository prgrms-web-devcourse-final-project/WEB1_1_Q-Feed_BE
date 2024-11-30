package com.wsws.moduledomain.category;

import com.wsws.moduledomain.category.vo.CategoryName;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;
}
