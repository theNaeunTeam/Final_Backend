package com.douzone.final_backend.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserDTO implements UserDetails {
    private String token;
    private String u_id; // 사용자 id
    private String u_pw; // 비밀번호
    private String u_cellPhone; // 사용자 전화번호
    private String u_email; // 사용자 이메일
    private String u_gender; // 사용자 성별
    private int u_age; // 사용자 나이

    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        return u_pw;
    }

    @Override
    public String getUsername() {
        return u_id;
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
