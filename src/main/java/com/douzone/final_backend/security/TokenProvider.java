package com.douzone.final_backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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


    public String create(String id, String role) {
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
//    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
//        //CustomDetailsService 에서 loadUserByUsername 재정의
//        UserDetails userDetails = userDetailsService.loadUserByUsername(token);
//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//    }

    // 토큰에서 id 가져오기 한다. customDetailService에서 호출함
    public String getUserPk(String token) {
        log.info(Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject());
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject();
    }




}
