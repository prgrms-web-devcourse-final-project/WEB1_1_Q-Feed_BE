package com.wsws.moduledomain.notification;

import com.wsws.moduledomain.group.vo.GroupId;
import com.wsws.moduledomain.notification.vo.NotificationContent;
import com.wsws.moduledomain.notification.vo.NotificationId;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    private NotificationId notificationId;
    private String type;
    private UserId sender;
    private UserId recipient;
    private boolean isRead;
    private NotificationContent content;
    private LocalDateTime createdAt;
    private Long targetId; // 게시글 ID 또는 댓글이 달린 게시글 ID
    private Long commentId; // 댓글 ID (댓글 알림인 경우)
    private GroupId groupId; // 그룹 ID (그룹 알림인 경우)
    private String url;

    public static Notification create(Long notificationId,String type, String sender, String recipient, String content, Long targetId, Long commentId, Long groupId, String url ) {
        Notification notification = new Notification();
//        notification.notificationId = NotificationId.of(notificationId);
        notification.type = type;
        notification.sender = UserId.of(sender);
        notification.recipient = UserId.of(recipient);
        notification.isRead = false;
        notification.content = NotificationContent.from(content);
        notification.targetId = targetId;
        notification.commentId = commentId;
        notification.groupId = GroupId.of(groupId);
        notification.url = url;
        return notification;
    }


    public void markAsRead() {
        this.isRead = true;
    }
}
