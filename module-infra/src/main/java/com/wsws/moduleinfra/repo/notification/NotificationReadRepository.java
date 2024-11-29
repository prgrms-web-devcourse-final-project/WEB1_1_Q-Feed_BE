package com.wsws.moduleinfra.repo.notification;

import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduleinfra.repo.notification.dto.NotificationResponseInfraDto;

import java.util.List;

public interface NotificationReadRepository {

    // 알림 조회
    List<NotificationResponseInfraDto> findByRecipientIdAndIsReadFalse(UserId recipientId);
}
