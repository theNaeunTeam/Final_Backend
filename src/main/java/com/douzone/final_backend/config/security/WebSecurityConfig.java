package com.douzone.final_backend.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http 빌더
        http.cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/static/**",
                        "/",
                        "/common/**",
                        "/manifest.json",
                        "/favicon.ico",
                        "/firebase-messaging-sw.js",
                        "/logo.png",
                        "/logo192.png",
                        "/logo512.png",
                        "/sitemap.xml",
                        "/service-worker.js",
                        "/service-worker.js.map",
                        "/asset-manifest.json",
                        "/robots.txt",
                        "/index.html"
                ).permitAll()
//                .antMatchers("/user/**").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER", "MASTER")
//                .antMatchers("/master/**").permitAll()
                .antMatchers("/master/**").hasAnyRole("MASTER")
                .antMatchers("/owner/**").hasAnyRole("OWNER")
//                .antMatchers("/owner/**").permitAll()
                .anyRequest()
                .authenticated();
        http.addFilterAfter(
                jwtAuthFilter,
                CorsFilter.class
        );
    }
}
