package com.douzone.final_backend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken((HttpServletRequest) request);
//            String token = parseBearerToken(request);
            log.info("Filter is running");
            log.info("token: " +token);
            if (token != null && !token.equalsIgnoreCase("null")) {
                String u_id = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user ID : " + u_id);
//                AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        u_id,
//                        null,
//                        AuthorityUtils.NO_AUTHORITIES //권한
//                );
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
//                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//                securityContext.setAuthentication(auth);
//                SecurityContextHolder.setContext(securityContext);

            }
        } catch (Exception ex) {
            log.error("Security context error", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("X-AUTH-TOKEN");
        log.info("bearedToken : "+bearerToken);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
//            return bearerToken.substring(7);
//        }
        return bearerToken;
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }
}
