package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesDTO {
    private String f_o_sNumber; // 사업자번호
    private String f_p_user_id; // 유저아이디

    private String o_name;
    private String o_address;
    private String o_time1;
    private String o_time2;
    private String o_phone;
    private int o_approval;

    private int startIndex;


}
