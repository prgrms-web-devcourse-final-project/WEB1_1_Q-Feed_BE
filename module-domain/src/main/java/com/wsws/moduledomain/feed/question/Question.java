package com.wsws.moduledomain.feed.question;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Question {
    @Id @GeneratedValue
    Long id;

    private String content;
    private boolean isToday;
    private LocalDateTime createdAt;


}
