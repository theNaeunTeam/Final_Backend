package com.douzone.final_backend.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 상품 예약 테이블
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveBean {

    private int r_code; // 예약 번호
    private String r_u_id; // 예약한 유저 id
    private int r_g_code; // 예약 상품 코드
    private String r_firstTime; // 방문 예약 시간
    private int r_status; // 예약 상태 0:상품등록상태 1:유저예약 2:가게승인 3:판매완료 4:취소 5:노쇼
    private String r_lastTime; // 판매시간 -> 예약 상태가 판매완료로 바뀌는 시간
    private String r_customOrder; //요청사항, 방문하는사람 등 기타사항
    private int r_count; // 해당 상품 몇개 예약했는지
    private String r_owner; // 해당 상품 가게의 사업자번호
}
