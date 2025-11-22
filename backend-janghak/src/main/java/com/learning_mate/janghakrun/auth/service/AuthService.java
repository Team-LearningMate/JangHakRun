package com.learning_mate.janghakrun.auth.service;

import com.learning_mate.janghakrun.auth.request.LoginRequest;
import com.learning_mate.janghakrun.auth.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public LoginResponse login(LoginRequest request) {
        // TODO: user 조회

        // TODO: 비밀번호 검증

        // TODO: JWT로 accessToken 생성

        String token = "";
        return LoginResponse.of(token);
    }
}
