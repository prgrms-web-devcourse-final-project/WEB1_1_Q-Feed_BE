package com.wsws.moduleinfra.repo.notification;

import com.wsws.moduleinfra.entity.notification.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("SELECT n FROM NotificationEntity n WHERE n.recipient.value = :recipientId AND n.isRead = false")
    List<NotificationEntity> findByRecipient_ValueAndIsReadFalse(@Param("recipientId") String recipientId);
}
