package com.learning_mate.janghakrun.user.repository;

import com.learning_mate.janghakrun.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 기본 crud만
}

