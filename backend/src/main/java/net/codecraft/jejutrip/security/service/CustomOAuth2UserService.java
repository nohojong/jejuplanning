package net.codecraft.jejutrip.security.service;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.user.constant.UserRole;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.account.user.repository.UserRepository;
import net.codecraft.jejutrip.security.oauth.CustomOAuth2User;
import net.codecraft.jejutrip.security.oauth.OAuthAttributes;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes oAuthAttributes =
                OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Map<String, Object> attributes = oAuthAttributes.getAttributes();

        String email = oAuthAttributes.getEmail();

        User user = saveOrUpdate(email);

        UserRole role = user.getRole();

        return CustomOAuth2User.builder()
                .email(email)
                .role(role)
                .attributes(attributes)
                .build();
    }

    private User saveOrUpdate(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(User.createOAuthUser(email));

        return userRepository.save(user);
    }
}
