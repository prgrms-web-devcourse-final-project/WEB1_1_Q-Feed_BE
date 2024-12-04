package com.wsws.moduleinfra.entity.user;

import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.user.vo.UserInterest;
import com.wsws.moduleinfra.entity.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserInterestEntityMapper {

    public UserInterestEntity toEntity(UserEntity userEntity, CategoryEntity categoryEntity) {
        return new UserInterestEntity(userEntity, categoryEntity);
    }

    public UserInterest toDomain(UserInterestEntity entity) {
        return UserInterest.create(CategoryId.of(entity.getCategory().getId()));
    }

    public List<UserInterest> toDomainList(List<UserInterestEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<UserInterestEntity> toEntityList(UserEntity userEntity, List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream()
                .map(category -> toEntity(userEntity, category))
                .collect(Collectors.toList());
    }
}
