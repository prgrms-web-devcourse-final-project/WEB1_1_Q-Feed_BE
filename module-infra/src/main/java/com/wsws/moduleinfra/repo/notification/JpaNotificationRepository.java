package com.wsws.moduleinfra.repo.notification;

import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduledomain.user.vo.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, Long>, NotificationRepository {

    @Override
    List<Notification> findByRecipientIdAndIsReadFalse(UserId recipientId);

}

