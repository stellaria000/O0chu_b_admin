package com.example.mariate.entity;

import com.example.mariate.dto.SignUpDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="users") // 해당 클래스를 Entity 클래스로 사용
@Table(name="users") // 데이터베이스에 있는 Table과 현재 클래스를 매핑
@Data
public class UsersEntity {

    @Id
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String gender;
    private String age;
    private String movieId;
    private String role;
    private String preference_1;
    private String preference_2;
    private String preference_3;

    @Lob
    private String token;

    public UsersEntity(SignUpDto dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.nickname = dto.getNickname();
        this.gender = dto.getGender();
        this.name = dto.getName();
        this.age = dto.getAge();
        this.movieId = dto.getMovieId();
        this.role = dto.getRole();
        this.preference_1 = dto.getPreference_1();
        this.preference_2 = dto.getPreference_2();
        this.preference_3 = dto.getPreference_3();
    }

}
