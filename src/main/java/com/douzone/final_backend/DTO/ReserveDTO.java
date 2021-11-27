package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ReserveDTO {

    private String r_firstDate; // 방문 예정 일
    private int r_code; // 예약 변호
    private int check;
    private String r_u_id;
    private int r_count;
    private int r_g_code; // 상품번호

    private String r_firstTime; // 방문 예정 시간
    private int r_status;
    private String r_customOrder;
    private int r_pay;
    private String r_owner; // o_sNumber

    // r_g_code로 검색한 상품 정보
    private String g_name;
    private int g_price;
    private int g_discount;
    private String g_expireDate;
    private String g_category;
    private int g_status;
    private int g_count;

    //    private GoodsDTO goodsDTO;
    private String searchInput;



}
