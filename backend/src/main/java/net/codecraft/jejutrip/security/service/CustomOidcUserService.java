package net.codecraft.jejutrip.security.service;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.user.constant.UserRole;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.account.user.repository.UserRepository;
import net.codecraft.jejutrip.security.oauth.CustomOAuth2User;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();         // Google 지원확인, scope에 openid, email 포함시

        Map<String, Object> attributes = oidcUser.getAttributes();

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