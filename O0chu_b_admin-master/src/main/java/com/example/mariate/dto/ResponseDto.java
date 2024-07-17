package com.example.mariate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "set")
public class ResponseDto<D> {
    // API 요청의 성공 여부를 나타내는 필드
    private boolean result;

    // 응답 메시지나 설명을 담는 필드
    private String message;

    // 실제 데이터를 담는 필드
    private D data;


    // 성공한 응답을 생성하는 정적 팩토리 메서드
    public static <D> ResponseDto<D> setSuccess(String message, D data) {

        return ResponseDto.set(true, message, data);
    }

    // 실패한 응답을 생성하는 정적 팩토리 메서드
    public static <D> ResponseDto<D> setFailed(String message) {
        return ResponseDto.set(false, message, null);
    }
}
