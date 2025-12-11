package net.codecraft.jejutrip.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.codecraft.jejutrip.security.jwt.dto.Token;
import net.codecraft.jejutrip.security.jwt.service.JwtService;
import net.codecraft.jejutrip.security.jwt.support.CookieSupport;
import net.codecraft.jejutrip.security.jwt.support.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
    private final CookieSupport cookieSupport;
    private final String clientUrl;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider,
                                              JwtService jwtService,
                                              CookieSupport cookieSupport,
                                              @Value("${client.url:http://localhost:5173}") String clientUrl) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtService = jwtService;
        this.cookieSupport = cookieSupport;
        this.clientUrl = clientUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 우리 서비스의 JWT 생성
        Token token = jwtTokenProvider.createJwtToken(oAuth2User.getEmail(), oAuth2User.getRole());

        // Refresh Token DB에 저장
        jwtService.login(token);

        // 쿠키에 토큰 저장
        cookieSupport.setCookieFromJwt(token, response);

        // 프론트엔드 URL로 리디렉션
        String targetUrl = clientUrl + "/oauth/redirect";

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}