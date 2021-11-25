package com.douzone.final_backend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken((HttpServletRequest) request);
//            String token = parseBearerToken(request);
            log.info("Filter is running");
            log.info("token: " + token);
            if (token != null && !token.equalsIgnoreCase("null")) {
                log.info("wwwww");
                String id = tokenProvider.getUserPk(token);
                log.info("Authenticated user ID : " + id);
                String u_id = id.substring(0,id.indexOf("&"));
                log.info("u_id : "+u_id);
                UserDetails userDetails = userDetailsService.loadUserByUsername(tokenProvider.validateAndGetUserId(token));
                log.info("uuuuuuuuu : "+userDetails);
//                AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        AuthorityUtils.NO_AUTHORITIES //권한
//                );
                UsernamePasswordAuthenticationToken auth = tokenProvider.getAuthentication(token);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(auth);
                SecurityContextHolder.setContext(securityContext);

            }
        } catch (Exception ex) {
            log.error("Security context error", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("X-AUTH-TOKEN");

        String bearerToken = request.getHeader("Authorization");
        log.info("bearedToken : " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }
}
