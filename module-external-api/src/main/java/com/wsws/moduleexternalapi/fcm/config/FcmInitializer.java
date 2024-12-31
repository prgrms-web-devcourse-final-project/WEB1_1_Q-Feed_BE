package com.wsws.moduleexternalapi.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class FcmInitializer {

    private static final String FIREBASE_CONFIG_PATH = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

    @PostConstruct
    public void initialize() {
        try {
            if (FIREBASE_CONFIG_PATH == null || FIREBASE_CONFIG_PATH.isEmpty()) {
                throw new IllegalStateException("환경 변수 GOOGLE_APPLICATION_CREDENTIALS가 설정되지 않았습니다.");
            }

            FileSystemResource resource = new FileSystemResource(FIREBASE_CONFIG_PATH);
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(resource.getInputStream());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(googleCredentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase 초기화 완료");
            }
        } catch (IOException e) {
            log.error("Firebase 초기화 실패: {}", e.getMessage());
            throw new IllegalStateException("Firebase 초기화 실패", e);
        }
    }
}
