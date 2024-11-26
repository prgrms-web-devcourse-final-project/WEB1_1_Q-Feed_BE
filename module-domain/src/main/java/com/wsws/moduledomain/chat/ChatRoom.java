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

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "user_id")))
    private UserId userId;

    @Embedded
    @AttributeOverrides( @AttributeOverride(name = "value", column = @Column(name = "user_id2")))
    private UserId userId2;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy="chatRoom", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(UserId userId, UserId userId2) {
        this.userId = userId;
        this.userId2 = userId2;
        this.createdAt = LocalDateTime.now();
    }

}
