package com.example.bank.global.config;

import com.example.bank.global.security.APIKeyAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.http.auth-token-header.name}")
    private String principalRequestHeader;

    @Value("${spring.http.auth-token}")
    private String principalRequestValue;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            if (!principalRequestValue.equals(principal))
                throw new BadCredentialsException("The API key was not validation");

            authentication.setAuthenticated(true);
            return authentication;
        });

        http

                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/bank/**", "/account/**", "/payment/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().formLogin().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://3.39.110.134:8083", "http://j9d210.p.ssafy.io:8081")); // 허용할 원본 도메인
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드
        configuration.setAllowCredentials(false); // 쿠키 등 자격 증명과 함께 요청을 할 수 있도록 허용
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // 허용할 헤더

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위에서 정의한 CORS 설정 적용
        return source;
    }


}
