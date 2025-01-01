package com.wsws.moduleexternalapi.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class FcmInitializer {

    @PostConstruct
    public void initialize() {
        try {
            log.info("Firebase 서비스 계정 키 파일을 로드합니다.");
            ClassPathResource resource = new ClassPathResource("firebase/firebase-service-key.json");

            try (InputStream serviceAccountStream = resource.getInputStream()) {
                GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccountStream);
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(googleCredentials)
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    log.info("Firebase 초기화가 완료되었습니다.");
                }
            }
        } catch (IOException e) {
            log.error("Firebase 초기화 중 오류 발생: {}", e.getMessage());
            throw new IllegalStateException("Firebase 초기화 실패", e);
        }
    }
}
