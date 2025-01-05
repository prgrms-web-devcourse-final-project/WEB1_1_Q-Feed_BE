package com.wsws.moduleinfra.socialnetworkcontext.interest.entity;


import com.wsws.moduleinfra.entity.CategoryEntity;
import com.wsws.moduleinfra.usercontext.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_interests")
public class UserInterestEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_interest_id")
    private Long id;


    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "category_id")
    private Long categoryId;

    public UserInterestEntity(String userId, Long categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }


}
