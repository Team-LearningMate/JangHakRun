    package com.learning_mate.janghakrun.config;

    import com.learning_mate.janghakrun.security.CustomUserDetails;
    import com.learning_mate.janghakrun.security.CustomUserDetailsService;
    import com.learning_mate.janghakrun.security.jwt.JwtAuthenticationFilter;
    import com.learning_mate.janghakrun.security.jwt.JwtTokenProvider;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final JwtTokenProvider jwtTokenProvider;
        private final CustomUserDetailsService customUserDetailsService;

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                       JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
            http
                    // CSRF 비활성화 (JWT 쓸 예정)
                    .csrf(AbstractHttpConfigurer::disable)

                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    // JWT 기반 인증만 사용
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)

                    // 요청 인가 규칙
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/v3/api-docs.yaml",

                                    "/auth/login"
                            ).permitAll()

                            .anyRequest().authenticated()
                    )
                    .userDetailsService(customUserDetailsService)

                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            ;

            return http.build();
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
