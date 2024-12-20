package com.wsws.moduleinfra.entity.notification;

import com.wsws.moduledomain.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationEntityMapper {

    // Entity -> Domain
    public Notification toDomain(NotificationEntity entity) {
        return Notification.create(
                entity.getId(),
                entity.getType(),
                entity.getSender(),
                entity.getRecipient(),
                entity.getContent(),
                null,
                null,
                null
        );
    }


    // Domain -> Entity
    public NotificationEntity toEntity(Notification notification) {
        return NotificationEntity.create(
                notification.getType(),
                notification.getContent().getValue(),
                notification.getSender().getValue(),
                notification.getRecipient().getValue()
        );
    }
}
