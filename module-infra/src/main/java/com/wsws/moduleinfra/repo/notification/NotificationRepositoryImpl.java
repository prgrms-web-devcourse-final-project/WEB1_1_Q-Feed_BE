package com.wsws.moduleinfra.repo.notification;


import com.wsws.moduleinfra.entity.notification.NotificationEntity;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduleinfra.entity.notification.NotificationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JpaNotificationRepository jpaRepository;
    private final NotificationEntityMapper mapper;

    @Override
    public List<Notification> findByRecipientIdAndIsReadFalse(String recipientId) {
        return jpaRepository.findByRecipientAndIsReadFalse(recipientId)
                .stream()
                .map(mapper::toDomain) // Entity → Domain 변환
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Notification> findById(Long notificationId) {
        return jpaRepository.findById(notificationId)
                .map(mapper::toDomain); // Entity → Domain 변환
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        NotificationEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity); // 저장된 Entity를 Domain으로 변환
    }
}
