package com.example.mariate.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class BoardController {

    // @GetMapping 어노테이션은 HTTP GET 요청을 처리하는 메서드를 정의합니다.
    @PostMapping("/logined")
    public String getBoard(@AuthenticationPrincipal String email) {
        // @AuthenticationPrincipal 어노테이션을 사용하여 현재 로그인한 사용자의 이메일 정보를 주입받습니다.
        // 이 정보는 Spring Security에서 현재 사용자의 주체(principal)를 나타냅니다.
        // 주체(principal)는 일반적으로 사용자의 식별 정보를 나타냅니다.

        // 반환되는 문자열은 현재 로그인한 사용자의 이메일을 포함한 메시지입니다.
        return "로그인된 사용자는 " + email + " 입니다.";
    }



}
