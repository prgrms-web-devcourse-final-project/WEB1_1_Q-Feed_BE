package com.wsws.moduleinfra.entity.notification;

import com.wsws.moduledomain.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationEntityMapper {

    // Entity -> Domain
    public Notification toDomain(NotificationEntity entity) {
        return new Notification(
                entity.getId(),
                entity.getType(),
                entity.getSender(),
                entity.getRecipient(),
                entity.isRead()
        );
    }

    // Domain -> Entity
    public NotificationEntity toEntity(Notification notification) {
        return new NotificationEntity.create(
                notification.getType(),
                notification.getSender(),
                notification.getRecipient(),
                notification.isRead()
        );
    }
}
