package com.wsws.moduleinfra.socialnetworkcontext.interest.entity;

import com.wsws.moduledomain.category.vo.CategoryId;
import com.wsws.moduledomain.socialnetwork.interest.UserInterest;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserInterestEntityMapper {

    public UserInterestEntity toEntity(String userId, Long categoryId) {
        return new UserInterestEntity(userId, categoryId);
    }

    public UserInterest toDomain(UserInterestEntity entity) {
        return UserInterest.create(CategoryId.of(entity.getCategoryId()), UserId.of(entity.getUserId()));
    }

    public List<UserInterest> toDomainList(List<UserInterestEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<UserInterestEntity> toEntityList(List<UserInterest> interests) {
        return interests.stream()
                .map(ui -> toEntity(ui.getUserId().getValue(), ui.getCategoryId().getValue()))
                .collect(Collectors.toList());
    }
}
