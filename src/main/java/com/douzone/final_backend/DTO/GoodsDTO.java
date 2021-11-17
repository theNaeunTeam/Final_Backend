package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsDTO {
    private String g_code; //상품 코드
    private String g_owner; // 상품 올린 사업자 번호
    private String g_name; // 상품 이름
    private String g_count; // 상품 수량
    private String g_price; // 상품 정가
    private String g_discount; // 상품 할인가
    private String g_detail; // 상세 설명
    private String g_image; // 상품 이미지
    private String g_expireDate; // 상품 유통기한
    private String g_status; //  상품 상태 (0판매중, 1판매완료,2판매완료)
    private String g_category; // 상품 카테고리
    private String actionType; // 새상품 등록인지 , 상품 업데이트인지
}
