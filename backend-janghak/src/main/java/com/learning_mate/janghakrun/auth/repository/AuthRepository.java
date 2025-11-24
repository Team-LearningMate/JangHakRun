package com.learning_mate.janghakrun.auth.repository;

import com.learning_mate.janghakrun.auth.domain.Auth;
import com.learning_mate.janghakrun.auth.domain.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    // LOCAL 로그인
    Optional<Auth> findByProviderAndEmail(AuthProvider provider, String email);

    // OAuth 로그인
    Optional<Auth> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);
}
