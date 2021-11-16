package com.douzone.final_backend.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReserveDTO {

    private int r_code; // 예약 변호
    private String check ;
    private String r_u_id;
    private int r_count ;
    private int r_g_code; // 상품번호

    private String r_owner; // o_sNumber

}
