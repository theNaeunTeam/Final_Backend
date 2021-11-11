package com.douzone.final_backend.Owner;

import lombok.Data;

// 사업자 테이블
@Data
public class OwnerBean {
    private String o_sNumber; // 사업자 번호
    private int o_approval; // 가맹 상태 0:승인대기 1:승인완료 2:승인거절 3:가맹해제
    private String o_pw; // 가게 로그인 비밀번호
    private String o_phone; // 가게 전화번호
    private String o_name; // 가게 이름
    private String o_cellPhone; // 사장 전화번호
    private String o_address; // 가게 주소
    private String o_latitude; //가게 위도
    private String o_longitude; // 가게 경도
    private String o_date; // 가입일
    private String o_time; // 영업 시간
    private String o_image; // 가게 대표 사진
}
