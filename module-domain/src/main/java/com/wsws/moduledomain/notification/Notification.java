package com.wsws.moduledomain.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    private Long id;
    private String type;
    private String sender;
    private String recipient;
    private boolean isRead;

    public Notification(Long id, String type, String sender, String recipient, boolean isRead) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.isRead = isRead;
    }


    public static Notification createNotification(String type, String sender, String recipient) {
        return new Notification(null, type, sender, recipient, false);
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
