package com.wsws.moduledomain.notification;

import com.wsws.moduledomain.user.vo.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    private Long id;
    private String type;
    private UserId sender;
    private UserId recipient;
    private boolean isRead;

    public Notification(Long id, String type, UserId sender, UserId recipient, boolean isRead) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.isRead = isRead;
    }

    public static Notification createNotification(String type, UserId sender, UserId recipient) {
        return new Notification(null, type, sender, recipient, false);
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
