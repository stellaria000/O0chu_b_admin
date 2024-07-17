package com.example.mariate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchUsersDto {

    private String email;
    private String nickname;
    private String password;
    private String name;
    private String age;
    private String gender;
    private String preference_1;
    private String preference_2;
    private String preference_3;

    private String oldPassword;
}