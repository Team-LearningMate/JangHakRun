    package com.learning_mate.janghakrun.config;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.learning_mate.janghakrun.auth.oauth.CustomOAuth2UserService;
    import com.learning_mate.janghakrun.auth.oauth.OAuth2LoginSuccessHandler;
    import com.learning_mate.janghakrun.global.error.ErrorCode;
    import com.learning_mate.janghakrun.global.error.ErrorResponse;
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
        private final CustomOAuth2UserService customOAuth2UserService;
        private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
        private final ObjectMapper objectMapper;

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

                                    "/api/v1/auth/login",
                                    "/oauth2/**",           // oauth 로그인
                                    "/login/oauth2/**"      // oauth callback
                            ).permitAll()

                            .anyRequest().authenticated()
                    )
                    .userDetailsService(customUserDetailsService)

                    // oauth 설정
                    .oauth2Login(oauth -> oauth
                                    .userInfoEndpoint(userInfo -> userInfo
                                    .userService(customOAuth2UserService)
                                    )
                                    .successHandler(oAuth2LoginSuccessHandler)
                            )

                    // 로그인이 필요한 페이지로 이동할 시 Error 반환
                    .exceptionHandling(e -> e
                            .authenticationEntryPoint((request, response, authException) -> {
                                ErrorResponse body = ErrorResponse.of(ErrorCode.AUTH_UNAUTHORIZED);

                                response.setStatus(body.httpStatus());
                                response.setContentType("application/json;charset=UTF-8");
                                response.getWriter().write(objectMapper.writeValueAsString(body));
                            })
                    )

                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            ;

            return http.build();
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
