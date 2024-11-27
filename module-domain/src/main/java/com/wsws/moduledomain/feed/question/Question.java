package com.wsws.moduledomain.feed.question;

import com.wsws.moduledomain.feed.Category.Category;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
