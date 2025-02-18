//package com.icia.rmate.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class FirebaseConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);
//
//    public FirebaseConfig() {
//        try {
//            // Firebase 초기화 여부 확인
//            if (FirebaseApp.getApps().isEmpty()) {
//                // 서비스 계정 JSON 파일 경로
//                String serviceAccountFile = "src/main/resources/serviceAccountKey.json";
//                FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);
//
//                // Firebase 옵션 설정
//                FirebaseOptions options = new FirebaseOptions.Builder()
//                        .setCredentials(GoogleCredentials.fromStream(serviceAccount)) // com.google.auth.oauth2 사용
//                        .setDatabaseUrl("https://rmate-d0258-default-rtdb.firebaseio.com/")
//                        .build();
//
//                // Firebase 초기화
//                FirebaseApp.initializeApp(options);
//                logger.info("Firebase가 성공적으로 초기화되었습니다.");
//            } else {
//                logger.info("Firebase는 이미 초기화되었습니다.");
//            }
//
//        } catch (IOException e) {
//            logger.error("Firebase 설정 로드 중 IO 오류가 발생했습니다: {}", e.getMessage());
//            throw new IllegalStateException("Firebase 설정 로드 오류", e);
//
//        } catch (Exception e) {
//            logger.error("Firebase 초기화 중 예기치 못한 오류가 발생했습니다: {}", e.getMessage());
//            throw new IllegalStateException("Firebase 초기화 실패", e);
//        }
//    }
//}
