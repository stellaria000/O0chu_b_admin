package com.example.mariate.service;

import com.example.mariate.dto.*;
import com.example.mariate.entity.ScoreEntity;
import com.example.mariate.entity.UsersEntity;
import com.example.mariate.repository.ScoreRepository;
import com.example.mariate.repository.UsersRepository;
import com.example.mariate.security.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsersEntity findUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    // 사용자 회원가입을 처리하는 메서드
    public ResponseDto<?> signUp(SignUpDto dto) {
        String password = dto.getPassword();


        // UsersEntity 생성
        UsersEntity usersEntity = new UsersEntity(dto);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        usersEntity.setPassword(encodedPassword);

        // Role 설정
        if (dto.getRole() == null || dto.getRole().isEmpty()) {
            usersEntity.setRole("0");  // 기본 역할을 지정
        } else {
            usersEntity.setRole(dto.getRole());
        }

        // UserRepository를 이용해서 데이터베이스에 Entity 저장
        try {
            usersRepository.save(usersEntity);

            // 회원가입 후 Score 테이블에 데이터 저장
            String userEmail = usersEntity.getEmail();
            String selectedMovieId = usersEntity.getMovieId();
            saveScoreForUser(userEmail, selectedMovieId);
        } catch (Exception e) {
            logger.error("데이터베이스 오류 발생", e);
            return ResponseDto.setFailed("데이터베이스 오류");
        }

        // 성공시 Success Response 반환
        return ResponseDto.setSuccess("SignUp Success", null);
    }

    private void saveScoreForUser(String userEmail, String movieId) {
        // null인 경우 또는 숫자가 아닌 문자열인 경우 pass(admin 생성되는거 때매 조건 걸어둠)
        if(movieId == null || !movieId.matches("\\d+")) {
            return;
        }
        ScoreEntity scoreEntity = ScoreEntity.builder()
                .user_email(userEmail)
                .movie_id(Long.parseLong(movieId))
                .score(5)
                .times(LocalDateTime.now())
                .build();
        scoreRepository.save(scoreEntity);
    }

    // 사용자 로그인을 처리하는 메서드
    public ResponseDto<SignInResponseDto> signIn(SignInDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        UsersEntity usersEntity;

        try {
            // 이메일로 사용자를 검색
            usersEntity = usersRepository.findByEmail(email);
            if (usersEntity == null)
                return ResponseDto.setFailed("로그인 실패");

            // 비밀번호 일치 여부 검증
            if (!passwordEncoder.matches(password, usersEntity.getPassword()))
                return ResponseDto.setFailed("로그인 실패");
        } catch (Exception error) {
            return ResponseDto.setFailed("데이터베이스 오류");
        }

        int expirationTime = 3600000; // 토큰 만료 시간 (1시간)

        // usersEntity에서 role 가져오기.
        String role = usersEntity.getRole();
        String nickname = usersEntity.getNickname();

        // JWT 토큰 생성 시 email과 role 정보도 포함
        String token = tokenProvider.create(email, role, nickname);

        // UsersEntity에 토큰 정보 저장
        usersEntity.setToken(token);

        usersRepository.save(usersEntity);

        SignInResponseDto signInResponseDto = new SignInResponseDto(token, expirationTime, usersEntity, role);
        return ResponseDto.setSuccess("로그인 성공", signInResponseDto);
    }

    public ResponseDto<?> nextClick(NextClickDto dto) {
        String password = dto.getPassword();
        String passwordCheck = dto.getPasswordCheck();
        String email = dto.getEmail();
        String nickname = dto.getNickname();

        // email 중복 확인
        try {
            if (usersRepository.findByEmail(email) != null)
                return ResponseDto.setFailed("Email already in use");
        } catch (Exception error) {
            return ResponseDto.setFailed("Database Error - Email");
        }

        try {
            if (usersRepository.existsByNickname(nickname))
                return ResponseDto.setFailed("Nickname already in use");
        } catch (Exception error) {
            return ResponseDto.setFailed("Database Error - Nickname");
        }

        // 비밀번호가 서로 다르면 failed response 변환
        if (password == null || !password.equals(passwordCheck)) { // 수정: 비밀번호 비교 시 대소문자 구분으로 변경
            return ResponseDto.setFailed("비밀번호가 다릅니다.");
        }

        // 모든 조건을 만족하면 success response 반환
        return ResponseDto.setSuccess("모든 조건이 충족되었습니다.", null);
    }
}