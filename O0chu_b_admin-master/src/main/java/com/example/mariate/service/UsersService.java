package com.example.mariate.service;

import com.example.mariate.dto.PatchUsersDto;
import com.example.mariate.dto.ResponseDto;
import com.example.mariate.entity.UsersEntity;
import com.example.mariate.dto.PatchUsersResponseDto;
import com.example.mariate.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    private final String nicknameRegex = "^[ㄱ-ㅎ|가-힣|a-zA-Z0-9].{1,8}$";


    public ResponseDto<PatchUsersResponseDto> getUserInfo(String email){
        UsersEntity usersEntity = null;

        try {
            usersEntity = usersRepository.findByEmail(email);
            if (usersEntity == null) {
                return ResponseDto.setFailed("Does Not Exist User");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.setFailed("데이터베이스 오류");
        }

        PatchUsersResponseDto patchUsersResponseDto = new PatchUsersResponseDto(usersEntity);

        return ResponseDto.setSuccess("Success", patchUsersResponseDto);
    }

    public ResponseDto<PatchUsersResponseDto> deleteUser(String email, String password) {
        UsersEntity usersEntity = usersRepository.findByEmail(email);
        if (usersEntity == null) {
            return ResponseDto.setFailed("Does Not Exist User");
        }

        if (!passwordEncoder.matches(password, usersEntity.getPassword())) {
            return ResponseDto.setFailed("Current Password Does Not Match");
        }

        try {
            usersRepository.delete(usersEntity);
            return ResponseDto.setSuccess("회원 탈퇴 성공", null);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.setFailed("데이터베이스 오류");
        }
    }

    public ResponseDto<PatchUsersResponseDto> updateNickname(PatchUsersDto dto, String email) {
        UsersEntity usersEntity = null;
        String nickname = dto.getNickname();

        try {
            usersEntity = usersRepository.findByEmail(email);
            if (usersEntity == null) {
                return ResponseDto.setFailed("Does Not Exist User");
            }

            // 패턴 검사
            if (!nickname.matches(nicknameRegex)) {
                return ResponseDto.setFailed("Invalid Nickname Pattern");
            }

            // 닉네임 중복 검사
            UsersEntity existingUserWithNickname = usersRepository.findByNickname(nickname);
            if (existingUserWithNickname != null) {
                return ResponseDto.setFailed("Nickname already in use");
            }

            usersEntity.setNickname(nickname);
            usersRepository.save(usersEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.setFailed("데이터베이스 오류");
        }

        PatchUsersResponseDto patchUsersResponseDto = new PatchUsersResponseDto(usersEntity);

        return ResponseDto.setSuccess("Success", patchUsersResponseDto);
    }

    public ResponseDto<PatchUsersResponseDto> updatePassword(PatchUsersDto dto, String email) {
        UsersEntity usersEntity = null;
        String newPassword = dto.getPassword();
        try {
            usersEntity = usersRepository.findByEmail(email);
            if (usersEntity == null) {
                return ResponseDto.setFailed("Does Not Exist User");
            }

            // 기존 비밀번호의 유효성 확인
            if (!isOldPasswordValid(email, dto.getOldPassword())) {
                // 기존 비밀번호가 유효하지 않을 경우 에러 응답 반환
                return ResponseDto.setFailed("Current Password Does Not Match");
            }

            // 패턴 검사
            if (!newPassword.matches(passwordRegex)) {
                return ResponseDto.setFailed("Invalid Password Pattern");
            }

            // 비밀번호 변경을 위한 암호화
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            System.out.println(encodedNewPassword);
            usersEntity.setPassword(encodedNewPassword);
            usersRepository.save(usersEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.setFailed("데이터베이스 오류");
        }

        PatchUsersResponseDto patchUsersResponseDto = new PatchUsersResponseDto(usersEntity);

        return ResponseDto.setSuccess("Success", patchUsersResponseDto);
    }

    public ResponseDto<PatchUsersResponseDto> updatePreference(PatchUsersDto dto, String email) {
        UsersEntity usersEntity = null;

        String preference_1 = dto.getPreference_1();
        String preference_2 = dto.getPreference_2();
        String preference_3 = dto.getPreference_3();

        try {
            usersEntity = usersRepository.findByEmail(email);
            if (usersEntity == null) {
                return ResponseDto.setFailed("Does Not Exist User");
            }

            usersEntity.setPreference_1(preference_1);
            usersEntity.setPreference_2(preference_2);
            usersEntity.setPreference_3(preference_3);

            usersRepository.save(usersEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.setFailed("데이터베이스 오류");
        }

        PatchUsersResponseDto patchUsersResponseDto = new PatchUsersResponseDto(usersEntity);

        return ResponseDto.setSuccess("Success", patchUsersResponseDto);
    }

    public boolean isOldPasswordValid(String email, String oldPassword) {
        UsersEntity usersEntity = usersRepository.findByEmail(email);
        if (usersEntity == null) {
            return false; // 사용자가 존재하지 않는 경우
        }

        // BCryptPasswordEncoder를 사용하여 기존 비밀번호를 검증
        return passwordEncoder.matches(oldPassword, usersEntity.getPassword());
    }

}
