package com.douzone.final_backend.Bean;

import lombok.Data;

// 즐겨찾는 가게 테이블
@Data
public class FavoritesBean {
    private String f_o_sNumber; // 가게 사업자 번호
    private String f_p_user_id; // 사용자 id
}
