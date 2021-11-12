package com.douzone.final_backend.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private String token;
    private String u_id; // 사용자 id
    private String u_pw; // 비밀번호
    private String u_cellPhone; // 사용자 전화번호
    private  String u_email; // 사용자 이메일
    private String u_gender; // 사용자 성별
    private int u_age; // 사용자 나이

}
