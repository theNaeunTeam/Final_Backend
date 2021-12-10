package com.douzone.final_backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final UserDetailsService userDetailsService;


    public String create(String id) {
        // 기한은 지금부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );
        log.info("토큰 생성 완료");

        //JWT 토큰 생성
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(id)
                .setIssuer("APP")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();


    }

    // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        //CustomDetailsService 에서 loadUserByUsername 재정의
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 토근에서 getSubject 한다. (우리는 subject = id&USER 등)
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject();
    }

    // 헤더 Authorization 가져오기. 헤더 Authorization = token
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

}
