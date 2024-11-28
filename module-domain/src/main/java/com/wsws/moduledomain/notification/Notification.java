package com.wsws.moduledomain.notification;

import com.wsws.moduledomain.notification.vo.CreatedAt;
import com.wsws.moduledomain.notification.vo.NotificationContent;
import com.wsws.moduledomain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Embedded
    private NotificationContent content;

    @Embedded
    private CreatedAt createdAt;

    @Column(nullable = false)
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User recipient;

    // 알림 읽음 처리
    public void markAsRead() {
        this.isRead = true;
    }

    public Notification(NotificationType type, NotificationContent content, User sender, User recipient) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.createdAt = new CreatedAt();
        this.isRead = false;
    }
}
