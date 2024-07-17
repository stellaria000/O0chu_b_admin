package com.example.mariate.config;

import com.example.mariate.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    // SecurityFilterChain을 구성하는 메서드
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CORS (Cross-Origin Resource Sharing) 설정을 활성화
                .cors().and()
                // CSRF(Cross-Site Request Forgery) 공격 방지 기능 비활성화
                .csrf().disable()
                // HTTP Basic 인증 비활성화
                .httpBasic().disable()
                // 세션 관리 설정: 세션을 생성하지 않고 상태를 유지하지 않음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 요청에 대한 인가 설정
                .authorizeRequests()
                // "/", "/api/auth/**" 경로에 대한 요청은 모든 사용자에게 허용
                .antMatchers("/**", "/api/**","/api/users/**").permitAll()
                // 그 외의 모든 요청은 인증된 사용자에게만 허용
                .anyRequest().authenticated();

        // JwtAuthencationFilter를 UsernamePasswordAuthenticationFilter 이전에 추가
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 구성된 HttpSecurity를 반환
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
