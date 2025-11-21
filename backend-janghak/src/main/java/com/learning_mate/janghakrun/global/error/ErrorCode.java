package com.learning_mate.janghakrun.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 시스템
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYSTEM_001", "서버 에러가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "SYSTEM_002", "잘못된 요청입니다."),

    // 인증 인가
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증이 필요합니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "만료된 토큰입니다."),
    AUTH_INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "AUTH_003", "아이디 또는 비밀번호가 올바르지 않습니다."),

    // 도메인
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "DOMAIN_001", "요청한 리소스를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
