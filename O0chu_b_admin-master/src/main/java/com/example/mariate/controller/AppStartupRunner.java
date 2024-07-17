package com.example.mariate.controller;

import com.example.mariate.dto.SignUpDto;
import com.example.mariate.entity.UsersEntity;
import com.example.mariate.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AppStartupRunner implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Logger 인스턴스 생성
    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Override
    public void run(String...args) throws Exception {
        UsersEntity existingUser = authService.findUserByEmail("admin@example.com");
        if (existingUser == null) {  // 사용자가 존재하지 않는 경우만
            logger.info("Admin user not found in database. Creating a new one...");

            SignUpDto adminUser = new SignUpDto();
            adminUser.setEmail("admin");
            adminUser.setPassword("1234");
            adminUser.setPasswordCheck("1234");
            adminUser.setRole("1");
            adminUser.setGender("1");
            adminUser.setName("관리자");

            authService.signUp(adminUser);

            logger.info("Admin user created successfully with email: {}", adminUser.getEmail());
        } else {
            logger.info("Admin user already exists in the database.");
        }
    }
}