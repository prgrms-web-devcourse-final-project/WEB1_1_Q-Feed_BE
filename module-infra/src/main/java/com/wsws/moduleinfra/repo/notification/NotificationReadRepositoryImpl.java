package com.wsws.moduleinfra.repo.notification;

import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduleinfra.repo.notification.dto.NotificationResponseInfraDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationReadRepositoryImpl implements NotificationReadRepository {

    @Override
    public List<NotificationResponseInfraDto> findByRecipientIdAndIsReadFalse(UserId recipientId) {
        return List.of();
    }
}
