package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO{
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
//    private String o_time;
    private String o_time1; // 영업 시작 시간
    private String o_time2; // 영업 종료 시간
    private String o_image; // 가게 대표 사진

    private int goods;
    private int total;
    private int reserve;

    private String checkStatus;

    private List<String> selectedRow;


}
