package com.learning_mate.janghakrun.auth.oauth;

import com.learning_mate.janghakrun.auth.domain.Auth;
import com.learning_mate.janghakrun.auth.domain.AuthProvider;
import com.learning_mate.janghakrun.auth.repository.AuthRepository;
import com.learning_mate.janghakrun.user.domain.User;
import com.learning_mate.janghakrun.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 유저 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 실제 값 꺼내기
        String providerUserId = String.valueOf(attributes.get(userNameAttributeName));
        String email = (String) attributes.get("email");

        // 우리 user와 매핑
        // 현재는 google만 지원 (OAuth 추가 시 provider 별로 case 작성)
        Auth auth = authRepository
                .findByProviderAndProviderUserId(AuthProvider.GOOGLE, providerUserId)
                .orElseGet(() -> createUserAndAuth(providerUserId, email));

        Long userId = auth.getUser().getId();

        // SpringContext에 넣을 OAuth2User
        Map<String, Object> extendAttributes = Map.of(
                userNameAttributeName, providerUserId,
                "email", email,
                "userId", userId
        );

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                extendAttributes,
                userNameAttributeName
        );

    }

    /**
     * 처음 로그인하는 회원의 경우 회원가입
     */
    private Auth createUserAndAuth(String providerUserId, String email) {
        User user = User.builder()
                .email(email)
                .phoneNumber(null)
                .build();
        userRepository.save(user);

        Auth auth = Auth.builder()
                .user(user)
                .provider(AuthProvider.GOOGLE)
                .providerUserId(providerUserId)
                .email(email)
                .build();

        return authRepository.save(auth);
    }

}
