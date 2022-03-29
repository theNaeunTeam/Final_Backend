package com.douzone.final_backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MasterVO {
    private String m_id; // 운영자 아이디
    private String m_pw; // 운영자 비밀번호
}
