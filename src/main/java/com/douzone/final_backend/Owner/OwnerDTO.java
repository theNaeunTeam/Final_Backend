package com.douzone.final_backend.Owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO {
    private String o_sNumber;
    private int o_approval;
    private String o_pw;
    private String token;
    private String o_phone; // 가게 전화번호
    private String o_name; // 가게 이름
    private String o_cellPhone; // 사장 전화번호
    private String o_address; // 가게 주소
    private String o_latitude; //가게 위도
    private String o_longitude; // 가게 경도
    private String o_date; // 가입일
    private String o_time1; // 영업 시간
    private String o_time2;
    private String o_image; // 가게 대표 사진
}
