package com.example.mariate.controller;

import com.example.mariate.dto.PatchUsersDto;
import com.example.mariate.dto.ResponseDto;
import com.example.mariate.dto.PatchUsersResponseDto;
import com.example.mariate.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/mypage")
    public ResponseDto<PatchUsersResponseDto> getUserInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        System.out.println(email);

        ResponseDto<PatchUsersResponseDto> response = usersService.getUserInfo(email);

        return response;
    }

    @PostMapping("/mypage/deleteuser")
    public ResponseDto<PatchUsersResponseDto> deleteUser(@RequestBody String userInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper om = new ObjectMapper();
        ResponseDto<PatchUsersResponseDto> response = null;

        try{
            map = om.readValue(userInfo, Map.class);
        }catch(Exception e){
            e.printStackTrace();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        if(email.equals((String)map.get("email"))) {

            response = usersService.deleteUser(email, (String)map.get("password"));
        }else{
            response = ResponseDto.setFailed("Does Not Match User");
        }
        System.out.println(response.toString());
        return response;

    }

    @PutMapping("/mypage/nickname")
    public ResponseDto<PatchUsersResponseDto> updateNickname(@RequestBody PatchUsersDto requestBody) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        System.out.println(email);
        System.out.println(requestBody);

        // NicknameService의 updateNickname 메서드를 호출하여 닉네임 업데이트
        ResponseDto<PatchUsersResponseDto> response = usersService.updateNickname(requestBody, email);
        System.out.println(response);

        // 응답을 반환
        return response;
    }

    @PutMapping("/mypage/password")
    public ResponseDto<PatchUsersResponseDto> updatePassword(@RequestBody PatchUsersDto requestBody) {
        System.out.println(requestBody.toString());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        // 서버에서 oldPassword 검증
        boolean isOldPasswordValid = usersService.isOldPasswordValid(email, requestBody.getOldPassword());

        System.out.println(isOldPasswordValid);
        if (isOldPasswordValid) {
            ResponseDto<PatchUsersResponseDto> response = usersService.updatePassword(requestBody, email);
            return response;
        } else {
            // 기존 비밀번호가 유효하지 않을 경우 에러 응답 반환
            return ResponseDto.setFailed("Current Password Does Not Match");
        }
    }

    @PutMapping("/mypage/preference")
    public ResponseDto<PatchUsersResponseDto> updatePreference(@RequestBody PatchUsersDto requestBody) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        // PreferenceService의 updatePreference 메서드를 호출하여 선호도 업데이트
        ResponseDto<PatchUsersResponseDto> response = usersService.updatePreference(requestBody, email);

        return response;
    }
}

