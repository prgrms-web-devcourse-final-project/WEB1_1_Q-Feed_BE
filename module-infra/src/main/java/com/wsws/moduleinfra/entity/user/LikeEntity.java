package com.wsws.moduleinfra.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class LikeEntity {
    @Id @GeneratedValue
    Long id;
}
