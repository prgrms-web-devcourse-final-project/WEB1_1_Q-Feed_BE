package com.wsws.moduledomain.notification.dto;

public record NotificationDto(
        Long notificationId,
        String type,
        String content,
        String sender,
        String recipient,
        boolean isRead,
        String url,
        Long targetId, // 게시글 ID 또는 댓글이 달린 게시글 I
        Long commentId,// 댓글 ID
        Long groupId  // 그룹 ID
) {
}
