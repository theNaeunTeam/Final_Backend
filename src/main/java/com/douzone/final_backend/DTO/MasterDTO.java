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
public class MasterDTO{
    private String m_id; // 운영자 아이디
    private String m_pw; // 운영자 비밀번호
    private String token;
    private List<String> roles;


}
