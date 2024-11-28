package com.wsws.moduledomain.follow;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String followerId;

    @Column(nullable = false)
    private String followeeId;

    public Follow(String followerId, String followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public static Follow create(String followerId, String followeeId) {
        return new Follow(followerId, followeeId);
    }


}
