package com.douzone.final_backend.vo;

import lombok.Data;

// 찜 테이블

@Data
public class PickVO {
    private int p_goods_id; // 상품 코드
    private String p_user_id; // 유저 아이디
}
