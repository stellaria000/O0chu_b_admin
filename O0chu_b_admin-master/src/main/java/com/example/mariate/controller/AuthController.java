package com.example.mariate.controller;

import com.example.mariate.dto.*;
import com.example.mariate.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired AuthService authService;
    private static final String SECRET_KEY = "jwtseckey!@";

    // POST 메서드를 사용하여 사용자 등록을 처리하는 엔드포인트
    @PostMapping("/signUp")
    public ResponseDto<?> signUp(@RequestBody SignUpDto requestBody) {
        // AuthService를 사용하여 사용자 등록을 수행하고 결과를 ResponseDto로 반환
        ResponseDto<?> result = authService.signUp(requestBody);
        return result;
    }

    // POST 메서드를 사용하여 사용자 로그인을 처리하는 엔드포인트
    @PostMapping("/signIn")
    public ResponseDto<SignInResponseDto> signIn(@RequestBody SignInDto requestBody) {
        // AuthService를 사용하여 사용자 로그인을 수행하고 결과를 ResponseDto로 반환

        ResponseDto<SignInResponseDto> result = authService.signIn(requestBody);
        System.out.println(result.getData());
        return result;

    }

    // POST 메서드를 사용하여 유효성 검사 처리하는 엔드포인트
    @PostMapping("/nextClick")
    public ResponseDto<?> nextClick(@RequestBody NextClickDto requestBody) {
        // AuthService를 사용하여 사용자 등록을 수행하고 결과를 ResponseDto로 반환
        ResponseDto<?> result = authService.nextClick(requestBody);
        return result;
    }


    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String token) {
        System.out.println(token + " :dd");

        Map<String, Object> userInfo = new HashMap<>();
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();

            String username = claims.getSubject();
            String nickname = claims.get("nickname", String.class);
            String role = claims.get("role", String.class);

            System.out.println(claims);

            // 여기에서 사용자 정보를 데이터베이스 또는 다른 소스에서 가져오는 로직을 추가해야 합니다.
            // 실제로는 사용자 정보 객체를 반환해야 하며, JSON 형식으로 반환할 수 있습니다.


            userInfo.put("email", username);
            userInfo.put("role", role);
            userInfo.put("nickname", nickname);
            userInfo.put("isAuth", true);


            return ResponseEntity.ok(userInfo);
        } else {
            userInfo.put("isAuth", false);
        }


        return ResponseEntity.ok(userInfo);

//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    }
}

