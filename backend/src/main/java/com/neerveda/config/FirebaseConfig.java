package com.neerveda.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);
    private boolean firebaseEnabled = false;

    @Bean
    public Firestore firestore() {
        try {
            ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
            if (resource.exists()) {
                InputStream serviceAccount = resource.getInputStream();
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
                firebaseEnabled = true;
                logger.info("Firebase Admin SDK initialized successfully. Connecting to Firestore...");
                return FirestoreClient.getFirestore();
            } else {
                logger.warn("firebase-service-account.json was not found in src/main/resources/. Backend will run in IN-MEMORY DATABASE mode.");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize Firebase SDK: {}. Backend will run in IN-MEMORY DATABASE mode.", e.getMessage());
        }
        return null;
    }

    public boolean isFirebaseEnabled() {
        return firebaseEnabled;
    }
}
