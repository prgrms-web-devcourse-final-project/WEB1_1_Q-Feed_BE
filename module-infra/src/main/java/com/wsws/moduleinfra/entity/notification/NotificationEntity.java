package com.wsws.moduleinfra.entity.notification;

import com.wsws.moduledomain.user.vo.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String sender;

    private String recipient;

    private boolean isRead;

    public static class create extends NotificationEntity {
        public create(String type, String sender, String recipient, boolean read) {
        }

    }
}
