package com.wsws.moduledomain.chat;

import com.wsws.moduledomain.chat.exception.SelfSelectionNotAllowedException;
import com.wsws.moduledomain.user.vo.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userId2;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy="chatRoom", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(String userId, String userId2) {
        this.userId = userId;
        this.userId2 = userId2;
        this.createdAt = LocalDateTime.now();
    }

    public static ChatRoom create(String userId, String userId2) {
        if (userId.equals(userId2)) {
            throw  SelfSelectionNotAllowedException.EXCEPTION;
        }

        // 항상 userId < userId2 순서로 설정
        if (userId.compareTo(userId2) > 0) {
            // userId가 userId2보다 클 경우 순서를 바꿈
            return new ChatRoom(userId2, userId);
        } else {
            // userId가 userId2보다 작거나 같을 경우 그대로 사용
            return new ChatRoom(userId, userId2);
        }
    }

}
