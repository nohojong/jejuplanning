package net.codecraft.jejutrip.security.jwt.support;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.security.jwt.dto.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CookieSupport {

    @Value("${server.url}")
    private String DOMAIN_URL;

    private ResponseCookie.ResponseCookieBuilder getBaseCookieBuilder(String name, String value) {
        return ResponseCookie.from(name, value)
                .path("/")
                .secure(false)              // 배포시 true
                .sameSite("lax")            // 배포시 none, lax 중 결정
                .httpOnly(true)
                .domain(DOMAIN_URL);
    }

    public ResponseCookie createAccessToken(String access) {
        return getBaseCookieBuilder("accessToken", access)
                .maxAge(30 * 60)
                .build();
    }

    public ResponseCookie createRefreshToken(String refresh) {
        return getBaseCookieBuilder("refreshToken", refresh)
                .maxAge(14 * 24 * 60 * 60)
                .build();
    }

    public void setCookieFromJwt(Token token , HttpServletResponse response) {
        response.addHeader("Set-Cookie" , createAccessToken(token.getAccessToken()).toString());
        response.addHeader("Set-Cookie" , createRefreshToken(token.getRefreshToken()).toString());
    }

    public void deleteJwtTokenInCookie(HttpServletResponse response) {

//        ResponseCookie accessToken = getBaseCookieBuilder("accessToken", "")
//                .maxAge(0)
//                .build();
//
//        ResponseCookie refreshToken = getBaseCookieBuilder("refreshToken", "")
//                .maxAge(0)
//                .build();
//
//        response.addHeader("Set-Cookie", accessToken.toString());
//        response.addHeader("Set-Cookie", refreshToken.toString());

        ResponseCookie accessTokenCookie = getBaseCookieBuilder("accessToken", "")
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = getBaseCookieBuilder("refreshToken", "")
                .maxAge(0)
                .build();

        log.info("Generated Set-Cookie for access token: {}", accessTokenCookie.toString());
        log.info("Generated Set-Cookie for refresh token: {}", refreshTokenCookie.toString());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        log.info("Successfully added Set-Cookie headers for deletion.");

    }
}
