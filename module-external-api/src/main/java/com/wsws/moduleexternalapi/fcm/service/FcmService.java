package com.wsws.moduleexternalapi.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduleexternalapi.fcm.dto.FCMRequestDto;
import com.wsws.moduleinfra.FcmRedis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {

      private final FcmRedis fcmRedis;
      private final UserRepository userRepository;

    @Async("taskExecutor")
    public void FcmSend(String recipient, FCMRequestDto fcmRequestDto) {

        String fcmRedisKey = getFcmRedisKey(recipient); // Redis 키 생성.
        String fcmToken = fcmRedis.getFcmToken(fcmRedisKey); // Redis에서 FCM 토큰 조회.

        if (fcmToken != null && !fcmToken.isEmpty()) { // 토큰이 존재하면 메시지 생성 후 전송.
            Message message = makeMessage(fcmRequestDto, fcmToken);
            sendMessage(message);
        }
    }

    // 메시지 생성
    public Message makeMessage(FCMRequestDto fcmRequestDto, String token){ // 나중에 토큰도 추가
        Notification.Builder notificationBuilder =
                Notification.builder()
                        .setTitle(fcmRequestDto.title())
                        .setBody(fcmRequestDto.body());

        return Message.builder()
                .setNotification(notificationBuilder.build())
                .setToken(token)
                .build();
    }

    // 메시지 보내기
    public void sendMessage(Message message) {
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("fcm 전송 오류");
        }
    }

    private String getFcmRedisKey(String userId) {
        return "FCM_TOKEN_" + userId; // Redis 키 형식 정의
    }

    //알림 제목 생성
    public String makeFcmTitle(String type) {
        return type + "알림"; // 예시) "좋아요 알림".
    }

    // 팔로우 알림 본문 생성
    public String makeFollowBody(String sender, String reportMember, String type) {
        return sender
                + " 님이 회원님을" + type + " 했습니다." ;
    }
    // 좋아요 알림 본문 생성
    public String makeLikeBody(String sender, String type) {
        return sender + " 님이 회원님의 글에" + type +"를 눌렀습니다." ;
    }
    // 채팅 알림 본문 생성
    public String makeChatBody(String sender, String type) {
        return sender + "님이"+ type +"을 보냈습니다.";
    }
    // 댓글 알림 본문 생성
    public String makeCommentBody(String sender, String type) {
        return sender + " 님이 회원님의 글에 "+ type +"을 남겼습니다.";
    }
    // Q-SPACE 내 좋아요 알림 본문 생성
    public String makeQLikeBody(String sender, String type) {
        return "Q_SPACE 멤버 "+ sender + " 님이 회원님의 글에" + type +"를 눌렀습니다.";
    }
    // Q-SPACE 댓글 알림 본문 생성
    public String makeQCommentBody(String sender, String type) {
        return "Q_SPACE 멤버 "+ sender + " 님이 회원님의 글에 "+ type +"을 남겼습니다.";
    }


}
