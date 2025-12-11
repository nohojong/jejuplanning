package net.codecraft.jejutrip.security.jwt.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.security.exception.TokenForgeryException;
import net.codecraft.jejutrip.security.jwt.domain.RefreshToken;
import net.codecraft.jejutrip.security.jwt.dto.Token;
import net.codecraft.jejutrip.security.jwt.repository.RefreshTokenRepository;
import net.codecraft.jejutrip.security.jwt.support.CookieSupport;
import net.codecraft.jejutrip.security.jwt.support.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static net.codecraft.jejutrip.security.jwt.domain.RefreshToken.createRefreshToken;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieSupport cookieSupport;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void login(Token token) {
        RefreshToken refreshToken = createRefreshToken(token);
        String loginUserEmail = refreshToken.getKeyEmail();

        refreshTokenRepository.existsByKeyEmail(loginUserEmail).ifPresent(a -> {
            refreshTokenRepository.deleteByKeyEmail(loginUserEmail);
        });

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
//    public void logout(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String refreshToken = getRefreshTokenFromHeader(request);
//            refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
//        } catch (SecurityException e) {
//            log.warn("Refresh token not found during logout, but continuing logout process: {}", e.getMessage());
//        }
//
//        cookieSupport.deleteJwtTokenInCookie(response);
//    }


    public RefreshToken getRefreshToken(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromHeader(request);

        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenForgeryException("알 수 없는 RefreshToken 입니다."));
    }

    public ResponseMessage validateRefreshToken(HttpServletRequest request , HttpServletResponse response) {
        try {
            RefreshToken token = getRefreshToken(request);
            String accessToken = jwtTokenProvider.validateRefreshToken(token);

            response.addHeader("Set-Cookie" , cookieSupport.createAccessToken(accessToken).toString());

            return ResponseMessage.of(ResponseCode.CREATE_ACCESS_TOKEN);
        } catch (NoSuchElementException e) {
            cookieSupport.deleteJwtTokenInCookie(response);
            throw new TokenForgeryException("변조된 RefreshToken 입니다.");
        }
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length != 0) {
            return Arrays.stream(cookies)
                    .filter(c -> c.getName().equals("refreshToken"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new SecurityException("인증되지 않은 사용자입니다."));
        }
        throw new SecurityException("인증되지 않은 사용자입니다.");
    }
}
