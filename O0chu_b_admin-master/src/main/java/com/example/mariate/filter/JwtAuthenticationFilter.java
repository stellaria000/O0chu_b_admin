package com.example.mariate.filter;

import com.example.mariate.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // HTTP 요청에서 Bearer 토큰을 추출합니다.
            String token = parseBearerToken(request);

            if (token != null && !token.equalsIgnoreCase("null")) {
                // 추출한 토큰을 사용하여 사용자 이메일을 검증합니다.
                String email = tokenProvider.validate(token);

                // 사용자 정보를 포함한 인증 객체를 생성합니다.
                AbstractAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Spring Security 컨텍스트에 인증 객체를 설정합니다.
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // 다음 필터 또는 요청 핸들러로 요청을 계속 전달합니다.
        filterChain.doFilter(request, response);
    }

    // HTTP 요청에서 Bearer 토큰을 추출하는 메서드
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
