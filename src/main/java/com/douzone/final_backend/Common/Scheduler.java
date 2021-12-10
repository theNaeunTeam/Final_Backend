package com.douzone.final_backend.Common;

import com.douzone.final_backend.Bean.GoodsBean;
import com.douzone.final_backend.Service.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class Scheduler {
    @Autowired
    private OwnerService ownerService;

    @Scheduled(cron = "0 0 0 * * * ") // 매일 새벽 12시에 실행
//    @Scheduled(cron = "*/3 * * * * *")
    public void run() {
        // 유통기한 확인
        // DB 에서 전체 상품 리스트의 유통기한 들고와서 반복적으로 오늘 날짜와 비교
        // 비교 후 날짜가 지났으면 판매 중지 2

        List<GoodsBean> goodsList = ownerService.allGoodsList();
        for (GoodsBean g : goodsList) {
            try {
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();

                String now_dt = date.format(now);

                // 오늘 날짜 parse
                Date date1 = date.parse(now_dt);

                // 기록된 유통기한 parse
                Date date2 = date.parse(g.getG_expireDate());

                // date2 가 date1 전? date2.before(date1)
                log.info("유통기한 : " + date2 + " / " + g.getG_expireDate());
                log.info("결과 " + date2.before(date1));
                if (date2.before(date1)) {
                    log.info("gcode" + g.getG_code());
                    // 판매 중지로 바꾸기
                    ownerService.changeStatus(g.getG_code());
                    // 해당 상품 예약이 있으면 거절로 바꾸기
                    ownerService.deleteStatus(g.getG_code());
                    log.info("했다 !!!");
                }

            } catch (Exception e) {

            }

        }
//        log.info("goodsList" + goodsList);

    }

}
