package com.wsws.moduledomain.chat;

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

    private LocalDateTime lastMessageTime;

    @OneToMany(mappedBy="chatRoom", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(String userId, String userId2) {
        this.userId = userId;
        this.userId2 = userId2;
        this.createdAt = LocalDateTime.now();
    }

    public static ChatRoom create(String userId, String userId2) {
        if(userId.equals(userId2)) {
            throw new IllegalArgumentException("자기 자신을 선택할 수 없습니다.");
        }
        return new ChatRoom(userId, userId2);
    }

    public void updateLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
