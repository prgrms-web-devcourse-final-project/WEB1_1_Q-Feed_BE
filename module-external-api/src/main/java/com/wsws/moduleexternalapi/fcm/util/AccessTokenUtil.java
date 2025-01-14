package com.wsws.moduleexternalapi.fcm.util;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AccessTokenUtil {
    private static final String FIREBASE_KEY_PATH = "firebase/firebase-service-key.json";

    public static String getAccessToken() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));


            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException("Access Token 발급 실패", e);
        }
    }

    public static void main(String[] args) {
        String accessToken = getAccessToken();
        log.info(accessToken);
    }
}
