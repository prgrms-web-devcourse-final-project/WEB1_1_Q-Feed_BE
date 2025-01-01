package com.wsws.moduleexternalapi.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class FcmInitializer {

    private static final String FIREBASE_CONFIG_PATH = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

    @PostConstruct
    public void initialize() {
        try {
            Resource resource;

            if (FIREBASE_CONFIG_PATH != null && !FIREBASE_CONFIG_PATH.isEmpty()) {
                log.info("환경 변수에서 Firebase 설정 파일 경로를 읽어옵니다: {}", FIREBASE_CONFIG_PATH);
                resource = new FileSystemResource(FIREBASE_CONFIG_PATH);
            } else {
                log.info("JAR 내부의 Firebase 설정 파일을 읽어옵니다.");
                resource = new ClassPathResource("firebase/firebase-service-key.json");
            }

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(resource.getInputStream());
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
