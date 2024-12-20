package com.wsws.moduledomain.notification.repo;

import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.dto.NotificationDto;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    List<NotificationDto> findByRecipientIdAndIsReadFalse(String recipientId);

    Optional<Notification> findById(Long notificationId);

    Notification save(Notification notification);

    void markAllAsReadByRecipientId(String recipientId);

    void edit(Notification notification);
}
