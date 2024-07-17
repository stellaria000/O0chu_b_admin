package com.example.mariate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    private String email;
    private String password;
    private String passwordCheck;
    private String name;
    private String nickname;
    private String gender;
    private String age;
    private String movieId;
    private String role;
    private String preference_1;
    private String preference_2;
    private String preference_3;

}
