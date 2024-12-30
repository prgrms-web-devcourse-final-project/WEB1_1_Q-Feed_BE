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

    private static final String FIREBASE_CONFIG_PATH = "module-external-api/src/main/resources/firebase/firebase-service-key.json";

    @PostConstruct
    public void initialize() {
        try {
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
