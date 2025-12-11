package net.codecraft.jejutrip.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${client.url:http://localhost:5173}") // application.yml 등에 프론트엔드 주소를 설정합니다.
    private String clientUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        log.error("OAuth2 Authentication failed: {}", exception.getMessage());

        String errorMessage;
        if (exception.getMessage().contains("authorization_request_not_found")) {
            errorMessage = "인증 요청을 찾을 수 없습니다. 다시 시도해 주세요.";
        } else {
            errorMessage = "로그인에 실패하였습니다. 원인: " + exception.getMessage();
        }

        // URL에 담을 수 있도록 에러 메시지를 UTF-8로 인코딩합니다.
        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        // 프론트엔드의 특정 에러 페이지로 리디렉션시킵니다.
        String targetUrl = UriComponentsBuilder.fromUriString(clientUrl + "/oauth/error")
                .queryParam("message", encodedErrorMessage)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
