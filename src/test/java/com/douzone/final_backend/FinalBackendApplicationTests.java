package com.douzone.final_backend;

import com.douzone.final_backend.DTO.ShopListDTO;
import com.douzone.final_backend.service.impl.CommonServiceImpl;
import com.douzone.final_backend.service.impl.RedisServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FinalBackendApplicationTests {

    @Autowired
    RedisServiceImpl redisServiceImpl;

    @Autowired
    CommonServiceImpl commonServiceImpl;

    @Test
    void contextLoads() {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("member", "valval222");
//        redisService.setRedisStringValue("test", "바나나");
//        redisService.getRedisStringValue("");
//        redisService.setRedisHashValue("testHash", hashMap);
//        redisService.setRedisGeoValue("testGeo", "cordddddd", 35.1604174298802, 129.16222733657);

        List<ShopListDTO> list = commonServiceImpl.selectRedisUpdate();
        list.forEach(value -> {
//            Map<String, String> map = new HashMap<>();
//            map.put("o_sNumber", value.getO_sNumber());
//            map.put("o_name", value.getO_name());
//            map.put("o_latitude", value.getO_latitude());
//            map.put("o_longitude", value.getO_longitude());
//            redisService.setRedisHashValue(value.getO_sNumber(), map);
            redisServiceImpl.setRedisGeoValue(
                    value.getO_sNumber(),
                    Double.parseDouble(value.getO_longitude()),
                    Double.parseDouble(value.getO_latitude()));
        });

        redisServiceImpl.getRedisGeoValue("loc_123", "", 129.16222733657, 35.1604174298802);

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
