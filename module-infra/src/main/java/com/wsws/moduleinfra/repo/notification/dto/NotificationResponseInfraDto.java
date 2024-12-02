package com.wsws.moduleinfra.repo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResponseInfraDto {
    private Long id;
    private String type;
    private String sender;
    private String recipient;
    private boolean isRead;
}
