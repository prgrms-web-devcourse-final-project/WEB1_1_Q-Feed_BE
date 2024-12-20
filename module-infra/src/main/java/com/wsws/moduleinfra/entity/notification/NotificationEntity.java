package com.wsws.moduleinfra.entity.notification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String sender;


    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRead;


    public static NotificationEntity create(String type, String content, String sender, String recipient) {
        NotificationEntity notification = new NotificationEntity();
        notification.type = type;
        notification.content = content;
        notification.sender = sender;
        notification.recipient = recipient;
        notification.isRead = false;
        notification.createdAt = LocalDateTime.now();
        return notification;
    }

    // 읽음 처리
    public void markAsRead() {
        this.isRead = true;
    }
}
