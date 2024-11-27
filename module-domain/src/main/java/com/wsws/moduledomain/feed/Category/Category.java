package com.wsws.moduledomain.feed.Category;

import com.wsws.moduledomain.follow.vo.CategoryName;
import jakarta.persistence.*;

@Entity
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;
}
