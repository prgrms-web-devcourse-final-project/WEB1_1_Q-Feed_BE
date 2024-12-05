package com.wsws.moduledomain.notification.repo;

import com.wsws.moduledomain.notification.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    List<Notification> findByRecipientIdAndIsReadFalse(String recipientId);

    Optional<Notification> findById(Long notificationId);

    Notification save(Notification notification);
}
