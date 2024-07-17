package com.example.mariate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class MainController {

    // @GetMapping 어노테이션은 HTTP GET 요청을 처리하는 메서드를 정의합니다.
    @GetMapping("/main")
    public String hello() {
        // 이 메서드는 "/" 경로에 대한 GET 요청을 처리하며 "Connection Successful" 문자열을 반환합니다.
        // 따라서 브라우저 또는 클라이언트에서 이 경로로 GET 요청을 보내면 "Connection Successful"이 응답으로 반환됩니다.
        return "Connection Successful";
    }

}
