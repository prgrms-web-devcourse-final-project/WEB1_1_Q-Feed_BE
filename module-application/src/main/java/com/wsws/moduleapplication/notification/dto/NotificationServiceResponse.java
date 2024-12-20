package com.wsws.moduleapplication.notification.dto;

import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.dto.NotificationDto;

public record NotificationServiceResponse(
        Long notificationId,
        String type,
        String content,
        String sender,
        String recipient,
        boolean isRead,
        String url
) {

    public NotificationServiceResponse(NotificationDto dto) {
        this(
                dto.notificationId(),
                dto.type(),
                dto.content(),
                dto.sender(),
                dto.recipient(),
                dto.isRead(),
                generateUrl(dto)
        );
    }

    // url 생성
    private static String generateUrl(NotificationDto notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }

        return switch (notification.type()) {
            case "FOLLOW" -> generateProfileUrl(notification.sender());
            case "CHAT" -> generateChatRoomUrl(notification.targetId());
            case "ANSWER_COMMENT" -> generateAnswerCommentUrl(notification.targetId(), notification.commentId());
            case "COMMENT_LIKE" -> generateCommentLikeUrl(notification.targetId(), notification.commentId());
            case "ANSWER_LIKE" -> generateAnswerUrl(notification.targetId());
            case "Q_SPACE_POST_COMMENT", "Q_SPACE_COMMENT_LIKE" -> generateGroupFeedCommentUrl(notification.groupId(), notification.targetId(), notification.commentId());
            case "Q_SPACE_POST_LIKE" -> generateGroupFeedUrl(notification.groupId(), notification.targetId());
            default -> "/";
        };
    }

    private static String generateProfileUrl(String sender) {
        if (sender == null || sender.isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be null or empty for FOLLOW notifications");
        }
        return "/users/" + sender; // 사용자 프로필 조회 엔드포인트를 활용
    }

    private static String generateChatRoomUrl(Long chatRoomId) {
        if (chatRoomId == null) {
            throw new IllegalArgumentException("Chat Room ID cannot be null for CHAT notifications");
        }
        return "/chats/" + chatRoomId + "/messages"; // 실제 채팅방 메시지 조회 URL
    }

    private static String generateAnswerCommentUrl(Long targetId, Long commentId) {
        if (targetId == null) {
            throw new IllegalArgumentException("Target ID cannot be null for ANSWER_COMMENT notifications");
        }
        return commentId != null
                ? "/feed/comments/" + commentId // 실제 답변 댓글 상세 URL
                : "/feed/answers/" + targetId; // 답변 기본 URL
    }

    private static String generateCommentLikeUrl(Long targetId, Long commentId) {
        if (targetId == null) {
            throw new IllegalArgumentException("Target ID cannot be null for COMMENT_LIKE notifications");
        }
        return commentId != null
                ? "/feed/comments/" + commentId // 댓글 좋아요 상세 URL
                : "/feed/answers/" + targetId; // 답변 기본 URL
    }

    private static String generateAnswerUrl(Long targetId) {
        if (targetId == null) {
            throw new IllegalArgumentException("Target ID cannot be null for ANSWER notifications");
        }
        return "/feed/answers/" + targetId; // 답변 상세 조회 URL
    }


    private static String generateGroupFeedCommentUrl(Long groupId, Long targetId, Long commentId) {
        if (groupId == null || targetId == null) {
            throw new IllegalArgumentException("Group ID and Target ID cannot be null for GROUP COMMENT notifications");
        }
        return commentId != null
                ? "/groups/posts/" + targetId + "#comment-" + commentId // 그룹 게시글 댓글 위치로 이동하는 URL
                : "/groups/posts/" + targetId; // 그룹 게시글 URL
    }

    private static String generateGroupFeedUrl(Long groupId, Long targetId) {
        if (groupId == null || targetId == null) {
            throw new IllegalArgumentException("Group ID and Target ID cannot be null for GROUP FEED notifications");
        }
        return "/groups/posts/" + targetId; // 그룹 게시글 URL
    }
}
