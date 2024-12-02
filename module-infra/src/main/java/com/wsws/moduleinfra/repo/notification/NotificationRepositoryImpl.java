package com.wsws.moduleinfra.repo.notification;


import com.wsws.moduleinfra.NotificationEntity;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduleinfra.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JpaNotificationRepository jpaRpository;
    private final NotificationMapper mapper;

    @Override
    public List<Notification> findByRecipientIdAndIsReadFalse(String recipientId) {
        return jpaRpository.findByRecipient_ValueAndIsReadFalse(recipientId)
                .stream()
                .map(mapper::toDomain) // Entity → Domain 변환
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Notification> findById(Long notificationId) {
        return jpaRpository.findById(notificationId)
                .map(mapper::toDomain); // Entity → Domain 변환
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        NotificationEntity savedEntity = jpaRpository.save(entity);
        return mapper.toDomain(savedEntity); // 저장된 Entity를 Domain으로 변환
    }
}
