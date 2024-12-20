package com.wsws.moduleinfra.repo.notification;

import com.wsws.moduleinfra.entity.notification.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("SELECT n FROM NotificationEntity n WHERE n.recipient = :recipientId AND n.isRead = false")
    List<NotificationEntity> findByRecipientAndIsReadFalse(@Param("recipientId") String recipientId);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.recipient = :recipientId AND n.isRead = false")
    void markAllAsReadByRecipientId(@Param("recipientId") String recipientId);
}
