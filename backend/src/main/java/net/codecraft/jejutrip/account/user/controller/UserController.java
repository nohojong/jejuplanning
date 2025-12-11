package net.codecraft.jejutrip.account.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.account.user.dto.LoginRequest;
import net.codecraft.jejutrip.account.user.dto.PasswordRequest;
import net.codecraft.jejutrip.account.user.service.UserService;
import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.security.jwt.repository.RefreshTokenRepository;
import net.codecraft.jejutrip.security.jwt.support.CookieSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieSupport cookieSupport;

    @PostMapping(value = "/auth/signup")
    public ResponseEntity<ResponseMessage> register(@RequestBody LoginRequest loginRequest)
            throws AccountException, IOException {
        return ResponseEntity.ok().body(userService.register(loginRequest));
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequest request , HttpServletResponse response) {
        userService.login(request, response);
        return ResponseEntity.ok().body(ResponseMessage.of(ResponseCode.LOGIN_SUCCESS));
    }

//    @PostMapping(value = "/auth/logout")
//    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
//        jwtService.logout(request, response);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping(value = "/auth/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 직접 refresh token 확인 및 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenRepository.findByToken(cookie.getValue())
                            .ifPresent(refreshTokenRepository::delete);
                    break;
                }
            }
        }
        cookieSupport.deleteJwtTokenInCookie(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/me")
    public ResponseEntity<String> getMyInfo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not Logged In");
        }
        return ResponseEntity.ok(principal.getName());
    }

    @DeleteMapping(value = "/users/me")
    public ResponseEntity deleteUser(@CookieValue String accessToken , HttpServletResponse response) {
        userService.removeUser(accessToken , response);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/users/me/password")
    public ResponseEntity modifyPassword(@RequestBody PasswordRequest request) {
        userService.modifyPassword(request);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/users/{email}")
    public ResponseEntity<ResponseMessage> isExistAccount(@PathVariable String email) {
        userService.isExistAccount(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/users")
    public ResponseEntity<ResponseMessage> findUserByToken(@CookieValue String accessToken) {
        return ResponseEntity.ok().body(userService.findUserByToken(accessToken));
    }

    @GetMapping(value = "/logout/message")
    public ResponseEntity<ResponseMessage> logout() {
        return ResponseEntity.ok().body(ResponseMessage.of(ResponseCode.LOGOUT_SUCCESS));
    }
}