package com.example.mariate.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {
    // JWT 생성 및 검증을 위한 키
    private static final String SECURITY_KEY = "jwtseckey!@"; // 비밀 키로 사용할 문자열

    // 주어진 이메일을 사용하여 JWT 토큰을 생성하는 메서드
    public String create(String email, String role, String nickname) {
        // 토큰의 만료 시간을 현재 시간으로부터 1시간 뒤로 설정
        Date expirationTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        // JWT 빌더를 사용하여 토큰을 생성하고 비밀 키로 서명
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                .setSubject(email)
                .claim("nickname", nickname)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .compact();
    }

    // 주어진 토큰을 유효성 검사하고 그에 따른 이메일 주소를 반환하는 메서드
    public String validate(String token) {
        // 토큰을 파싱하고 서명 검증을 수행하여 클레임(클레임 내용)을 얻음
        Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();

        // 클레임에서 주체(Subject) 정보를 추출하여 반환
        return claims.getSubject();
    }

    // 주어진 토큰에서 role 정보를 추출하는 메서드
    public String extractRole(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }
}
