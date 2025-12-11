package net.codecraft.jejutrip.security.oauth;

import lombok.Builder;
import lombok.Getter;
import net.codecraft.jejutrip.account.user.constant.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User, OidcUser {

    private final String email;
    private final UserRole role;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    @Builder
    public CustomOAuth2User(String email, UserRole role, Map<String, Object> attributes) {
        this.email = email;
        this.role = role;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role.getRole()));
        this.attributes = attributes;
    }

    @Override
    public String getName() { return this.email; }

    @Override
    public Map<String, Object> getAttributes() { return this.attributes;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Map<String, Object> getClaims() { return null;}

    @Override
    public OidcUserInfo getUserInfo() {return null;}

    @Override
    public OidcIdToken getIdToken() { return null;}
}
