    package com.learning_mate.janghakrun.auth.domain;

    import com.learning_mate.janghakrun.user.domain.User;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    @Entity
    @Table(name = "auth")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Auth {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Enumerated(EnumType.STRING)
        @Column(name = "provider", nullable = false, length = 20)
        private AuthProvider provider;

        // LOCAL 전용 로그인 이메일
        @Column(name = "email", length = 50)
        private String email;

        // LOCAL 전용 비밀번호 hash
        @Column(name = "password")
        private String password;

        // OAuth provider에서 주는 user id
        @Column(name = "provider_user_id", length = 100)
        private String providerUserId;

    }
