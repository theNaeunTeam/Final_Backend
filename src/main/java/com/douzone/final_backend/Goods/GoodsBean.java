package com.douzone.final_backend.Goods;

import lombok.Data;

// 상품 테이블
@Data
public class GoodsBean {
    private int g_code; //상품 코드
    private String g_owner; // 상품 올린 사업자 번호
    private String g_name; // 상품 이름
    private int g_count; // 상품 수량
    private int g_price; // 상품 정가
    private int discount; // 상품 할인가
    private String g_detail; // 상세 설명
    private String g_image; // 상품 이미지
    private String g_expireDate; // 상품 유통기한
    private int g_status; //  상품 상태 (0판매중, 1판매완료)
    private String g_category; // 상품 카테고리

}
