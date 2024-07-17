package com.example.mariate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NextClickDto {

    private String email;
    private String password;
    private String passwordCheck;
    private String name;
    private String nickname;
}
