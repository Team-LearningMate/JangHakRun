package com.learning_mate.janghakrun.auth.oauth;

import com.learning_mate.janghakrun.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String urlPrefix = "http://localhost:3000/oauth2/success?token=";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // principal에서 userId 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Long userId = ((Number) oAuth2User.getAttributes().get("userId")).longValue();

        String token = jwtTokenProvider.createToken(userId);

        String redirectUrl = urlPrefix + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }


}
