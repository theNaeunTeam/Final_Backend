package com.douzone.final_backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartDTO {
    private String o_name; // 가게 이름
    private String u_id; // 유저 아이디
    private int g_code; // 상품 고유번호
    private int g_count; // 상품 수량
    private int g_status; // 예약 상태
    private int g_price;
    private int g_discount;
    private String g_image;
    private String g_name;
}
