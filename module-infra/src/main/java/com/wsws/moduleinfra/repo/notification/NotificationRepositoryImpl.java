package com.wsws.moduleinfra.repo.notification;


import com.wsws.moduledomain.notification.dto.NotificationDto;
import com.wsws.moduleinfra.entity.notification.NotificationEntity;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduleinfra.entity.notification.NotificationEntityMapper;
import jakarta.transaction.Transactional;
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
    public List<NotificationDto> findByRecipientIdAndIsReadFalse(String recipientId) {
        return jpaRepository.findByRecipientAndIsReadFalse(recipientId)
                .stream()
                .map(entity -> new NotificationDto(
                        entity.getId(),
                        entity.getType(),
                        entity.getContent(),
                        entity.getSender(),
                        entity.getRecipient(),
                        entity.isRead(),
                        null, // URL은 ServiceResponse에서 생성
                        null, // targetId, commentId, groupId  엔티티에 저장x
                        null,
                        null
                ))
                .collect(Collectors.toList());
//                .stream()
//                .map(mapper::toDomain)
//                .collect(Collectors.toList());
    }

    @Override
    public Optional<Notification> findById(Long notificationId) {
        return jpaRepository.findById(notificationId)
                .map(mapper::toDomain);
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        NotificationEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void markAllAsReadByRecipientId(String recipientId) {
        jpaRepository.markAllAsReadByRecipientId(recipientId);
    }

    @Override
    @Transactional
    public void edit(Notification notification) {
        if (notification.getNotificationId() == null) {
            throw new IllegalArgumentException("Notification ID가 null입니다.");
        }

        NotificationEntity entity = jpaRepository.findById(notification.getNotificationId().getValue())
                .orElseThrow(() -> new RuntimeException("해당 알림을 찾을 수 없습니다."));

        // 읽음 상태로 수정
        entity.markAsRead();
    }
}
