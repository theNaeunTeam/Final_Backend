package com.douzone.final_backend.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 사용자 테이블
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserBean {
    private String u_id; // 사용자 id
    private String u_pw; // 비밀번호
    private String u_cellPhone; // 사용자 전화번호
    private String u_email; // 사용자 이메일
    private String u_gender; // 사용자 성별
    private int u_age; // 사용자 나이
    private int u_status; // 사용자 상태 0:기본 1:탈퇴 2:블랙리스트
    private int u_noShowCnt; // 노쇼 횟수. 로그인시 5이하인지 체크
}
