package com.wsws.moduledomain.notification.repo;

import com.wsws.moduledomain.notification.Notification;

import java.util.List;

public interface NotificationRepository {
    List<Notification> findByRecipientIdAndIsReadFalse(String recipientId);
    List<Notification> findByRecipientId(String recipientId);
}
