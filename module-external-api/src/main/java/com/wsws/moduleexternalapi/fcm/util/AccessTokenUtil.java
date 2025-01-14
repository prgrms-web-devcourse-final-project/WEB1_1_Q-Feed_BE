package com.wsws.moduleexternalapi.fcm.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
@Slf4j
public class AccessTokenUtil {
    private static final String FIREBASE_KEY_PATH = "firebase/firebase-service-key.json";
    private static GoogleCredentials googleCredentials;
    private static AccessToken cachedAccessToken;

    static {
        try {
            // Firebase 서비스 키 초기화
            googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));
        } catch (IOException e) {
            throw new RuntimeException("GoogleCredentials 초기화 실패", e);
        }
    }

    public static String getAccessToken() {
        try {
            // 기존 캐시된 토큰이 존재하고, 만료되지 않았다면 재사용
            if (cachedAccessToken != null && !isTokenExpired(cachedAccessToken)) {
                log.info("기존 엑세스 토큰  재사용. 만료 시간: {}", cachedAccessToken.getExpirationTime());
                return cachedAccessToken.getTokenValue();
            }

            // 토큰 갱신
            googleCredentials.refreshIfExpired();
            cachedAccessToken = googleCredentials.getAccessToken();

            log.info("새 엑세스 토큰 발급. 만료 시간: {}", cachedAccessToken.getExpirationTime());
            return cachedAccessToken.getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException("엑세스 토큰  발급 실패", e);
        }
    }

    private static boolean isTokenExpired(AccessToken token) {
        return token.getExpirationTime().toInstant().isBefore(Instant.now());
    }

    public static void main(String[] args) {
        // 간단한 access token 테스트
        String accessToken = getAccessToken();
        log.info("발급된 토큰: {}", accessToken);
    }
}
