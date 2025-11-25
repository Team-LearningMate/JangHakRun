package com.learning_mate.janghakrun.auth.service;

import com.learning_mate.janghakrun.auth.domain.Auth;
import com.learning_mate.janghakrun.auth.domain.AuthProvider;
import com.learning_mate.janghakrun.auth.repository.AuthRepository;
import com.learning_mate.janghakrun.auth.request.LoginRequest;
import com.learning_mate.janghakrun.auth.response.LoginResponse;
import com.learning_mate.janghakrun.global.error.ErrorCode;
import com.learning_mate.janghakrun.global.exception.BusinessException;
import com.learning_mate.janghakrun.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {

        // user 조회
        Auth auth = authRepository.findByProviderAndEmail(AuthProvider.LOCAL, request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS));

        // 비밀번호 검증
        if(!passwordEncoder.matches(request.password(), auth.getPassword())) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }

        // JWT로 accessToken 생성
        Long userId = auth.getUser().getId();
        String token = jwtTokenProvider.createToken(userId);

        return LoginResponse.of(token);
    }
}
