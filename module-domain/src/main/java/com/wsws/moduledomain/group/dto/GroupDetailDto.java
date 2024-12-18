package com.wsws.moduledomain.group.dto;

import com.wsws.moduledomain.category.vo.CategoryName;

import java.time.LocalDateTime;

public record GroupDetailDto(
        Long groupId,
        Long categoryId,
        CategoryName categoryName,
        String url,
        String groupName,
        String description,
        String adminId,
        LocalDateTime createdAt
) { }