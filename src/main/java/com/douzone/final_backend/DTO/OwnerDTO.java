package com.douzone.final_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO implements UserDetails {
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

    private List<String> roles;

    private String checkStatus;

    private List<String> selectedRow;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return o_pw;
    }

    @Override
    public String getUsername() {
        return o_sNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
