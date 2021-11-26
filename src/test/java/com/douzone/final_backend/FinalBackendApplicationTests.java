package com.douzone.final_backend;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class FinalBackendApplicationTests {

    @Test
    void contextLoads() {
//        List<String> registrationTokens = Arrays.asList(
//                "cDcOyHgStF_T-3Uf37fjPw:APA91bGRk86f_3Yw7ZaPgpIHIZTBaKQFH0j1R6c8ZEj08XEJjfQaVVa5A_C4WKtPdiBSBipKv-iXhY1JNbkTkRdWOr9lsPlPRT8L_wLd7Ga-6S3LroMXWGRkTkHhQZHaJ13wbvZiCLrW"
//        );
//
//        MulticastMessage message = MulticastMessage.builder()
//                .putData("title", "제품이름")
//                .putData("body", "예약시간ㄴㅁㅇㄹ")
//                .putData("etc", "ㄴㅇㄹㅇㄴㄹㅁㅇㄴㅁㄹ")
//                .addAllTokens(registrationTokens)
//                .build();
//        BatchResponse response = null;
//        try {
//            response = FirebaseMessaging.getInstance().sendMulticast(message);
//        } catch (FirebaseMessagingException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(response.getSuccessCount() + " 메시지 전송 결과");

    }

}
