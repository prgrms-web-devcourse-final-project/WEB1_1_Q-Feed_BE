package com.wsws.moduleexternalapi.fcm.service;

import com.wsws.moduleexternalapi.fcm.dto.FcmMessageRequestDto;
import com.wsws.moduleexternalapi.fcm.dto.FcmRequestDto;
import com.wsws.moduleexternalapi.fcm.util.AccessTokenUtil;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import com.wsws.moduleinfra.FcmRedis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {

    private final RestTemplate restTemplate;
    private final FcmRedis fcmRedis;



    @RabbitListener(queues = "fcmQueue")
    public void receiveMessage(@Payload FcmRequestDto fcmRequestDto) {
        log.info("🔄 RabbitMQ에서 메시지 수신: {} 타입의 알림", fcmRequestDto.type());

        try {
            // Redis 키 생성
            String fcmRedisKey = getFcmRedisKey(fcmRequestDto.recipient());
            log.info("FCM Redis Key 생성: {}", fcmRedisKey);

            // Redis에서 FCM 토큰 조회
            String fcmToken = fcmRedis.getFcmToken(fcmRedisKey);
            log.info("FCM Token 조회 결과: {}", fcmToken);

            if (fcmToken != null && !fcmToken.isEmpty()) {
                log.info("Fcm 토큰 있음");
                // 제목과 본문 생성
                String title = makeFcmTitle(fcmRequestDto.type());
                String body = makeFcmBody(fcmRequestDto.type(), fcmRequestDto.sender());

                FcmMessageRequestDto requestDto = new FcmMessageRequestDto(title, body);

                // 메시지 생성
                String message = makeMessage(fcmToken, requestDto);

                // 메시지 전송
                sendMessage(message);

            } else {
                log.warn("FCM 토큰이 없습니다. recipient={}, fcmRedisKey={}", fcmRequestDto.recipient(), fcmRedisKey);
            }
        } catch (Exception e) {
            log.error("FCM 전송 중 오류 발생: recipient={}, type={}, sender={}", fcmRequestDto.recipient(), fcmRequestDto.type(), fcmRequestDto.sender(), e);
        }
    }

    private String makeMessage(String targetToken, FcmMessageRequestDto fcmMessageRequestDto) {
        return """
                    {
                      "message": {
                        "token": "%s",
                        "notification": {
                          "title": "%s",
                          "body": "%s"
                        }
                      }
                    }
                """.formatted(targetToken, fcmMessageRequestDto.title(), fcmMessageRequestDto.body());
    }

    public void sendMessage(String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String accessToken = AccessTokenUtil.getAccessToken();
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            HttpEntity<String> httpEntity = new HttpEntity<>(message, headers);
            String projectId = "q-feed";
            String fcmRequestUrl = "https://fcm.googleapis.com/v1/projects/%s/messages:send";
            String url = String.format(fcmRequestUrl, projectId);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            if (responseEntity.getStatusCode().isError()) {
                log.error("FCM 전송 실패 : {} - {}",
                        responseEntity.getStatusCode(),
                        responseEntity.getBody());
            } else {
                log.info("FCM 전송 성공 : {}", responseEntity.getBody());
            }
        } catch (Exception e) {
            log.error("FCM 메시지 전송 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    private String getFcmRedisKey(String userId) {
        return "FCM_TOKEN_" + userId;
    }

    public String makeFcmBody(FcmType type, String sender) {
        return switch (type) {
            case FOLLOW -> sender + "님이 회원님을 팔로우했습니다.";
            case ANSWER_COMMENT -> sender + "님이 회원님의 글에 댓글을 남겼습니다.";
            case ANSWER_LIKE -> sender + "님이 회원님의 글을 좋아합니다.";
            case COMMENT_LIKE -> sender + "님이 회원님의 댓글을 좋아합니다.";
            case Q_SPACE_POST_COMMENT -> "Qspace 멤버 " + sender + "님이 회원님의 게시물에 댓글을 남겼습니다.";
            case Q_SPACE_POST_LIKE -> "Qspace 멤버 " + sender + "님이 회원님의 게시물을 좋아합니다.";
            case Q_SPACE_COMMENT_LIKE -> "Qspace 멤버 " + sender + "님이 회원님의 댓글을 좋아합니다.";
            case CHAT -> sender + "님이 회원님에게 새로운 메시지를 보냈습니다.";
            case GENERAL -> "알림이 도착했습니다.";
        };
    }

    private String makeFcmTitle(FcmType type) {
        return switch (type) {
            case FOLLOW -> "🔔팔로우 알림";
            case ANSWER_COMMENT -> "🔔Qfeed 댓글 알림";
            case ANSWER_LIKE -> "🔔Qfeed 좋아요 알림";
            case COMMENT_LIKE -> "🔔Qfeed 댓글 좋아요 알림";
            case Q_SPACE_POST_COMMENT -> "🔔Qspace 게시물 댓글 알림";
            case Q_SPACE_POST_LIKE -> "🔔Qspace 게시물 좋아요 알림";
            case Q_SPACE_COMMENT_LIKE -> "🔔Qspace 댓글 좋아요 알림";
            case CHAT -> "🔔채팅 알림";
            case GENERAL -> "🔔알림 모아보기";        };
    }

    public void sendFcm(String userId, String body, FcmType type) {

        try {
            String fcmToken = fcmRedis.getFcmToken(getFcmRedisKey(userId));
            if (fcmToken == null || fcmToken.isBlank()) {
                return;
            }

            String title = makeFcmTitle(type);
            FcmMessageRequestDto dto = new FcmMessageRequestDto(title, body);
            String message = makeMessage(fcmToken, dto);

            sendMessage(message);
        } catch (Exception e) {
            log.error("알림 전송 오류: userId={}, error={}", userId, e.getMessage(), e);
        }
    }

}
