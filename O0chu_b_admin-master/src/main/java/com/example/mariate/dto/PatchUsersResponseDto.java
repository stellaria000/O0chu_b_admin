package com.example.mariate.dto;

import com.example.mariate.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchUsersResponseDto {

    private UsersEntity users;

}
