package com.wsws.moduledomain.notification.repo;

import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.user.vo.UserId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository {
    List<Notification> findByRecipientIdAndIsReadFalse(UserId recipientId);
    Optional<Notification> findById(Long notificationId);
    Notification save(Notification notification);
}
