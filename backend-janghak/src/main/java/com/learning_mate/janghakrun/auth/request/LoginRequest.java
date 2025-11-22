package com.learning_mate.janghakrun.auth.request;

/*
 * 로그인 요청 DTO
 * 요청 필드 변경될 수 있음
 */
public record LoginReqeust(
        String id,
        String password
) {
}
