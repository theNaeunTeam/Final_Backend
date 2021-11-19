package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UserDTO {
    private String token;
    private String u_id; // 사용자 id
    private String u_pw; // 비밀번호
    private String u_cellPhone; // 사용자 전화번호
    private String u_email; // 사용자 이메일
    private String u_gender; // 사용자 성별
    private int u_age; // 사용자 나이
    private int u_status; // 회원상태 0 정상 1 탈퇴 2 블랙리스트
    private int u_point;

    private int save; // 구매 완료한 횟수
    private int reserve; //예약 완료

}
