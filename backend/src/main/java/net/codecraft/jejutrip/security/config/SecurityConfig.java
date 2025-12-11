package net.codecraft.jejutrip.security.config;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.user.constant.UserRole;
import net.codecraft.jejutrip.common.exception.FilterExceptionHandler;
import net.codecraft.jejutrip.security.jwt.support.JwtAuthenticationFilter;
import net.codecraft.jejutrip.security.oauth.CustomAuthenticationFailureHandler;
import net.codecraft.jejutrip.security.oauth.OAuth2AuthenticationSuccessHandler;
import net.codecraft.jejutrip.security.service.CustomOAuth2UserService;
import net.codecraft.jejutrip.security.service.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;
    private final FilterExceptionHandler filterExceptionHandler;
    private final CustomOAuth2UserService oauth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http    // CSRF, Form Login, HTTP Basic 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 세션 관리 정책을 STATELESS(세션 사용 안함)으로 설정
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // HTTP 요청 인가 설정
                .authorizeHttpRequests(auth -> auth

                    // Admin 경로는 MANAGER 권한 필요
                    .requestMatchers("/api/admin/**")
                    .hasRole(UserRole.MANAGER.name())

                    // 공개 엔드포인트 허용
                    .requestMatchers(
                            "/",
                            "/error",
                            "/favicon.ico",
                            "/api/auth/**",
                            "/oauth2/**",
                            "/login/oauth2/code/**")
                    .permitAll()

                    // 장소 조회는 인증 없이 허용
                    .requestMatchers(HttpMethod.GET, "/api/places/**")
                    .permitAll()

                    // 게시글 목록 조회는 인증 없이 허용
                    .requestMatchers(HttpMethod.GET, "/api/board/post")
                    .permitAll()

                    // 게시글 상세 조회는 인증 없이 허용
                    .requestMatchers(HttpMethod.GET, "/api/board/post/**")
                    .permitAll()

                    // 댓글 조회는 인증 없이 허용
                    .requestMatchers(HttpMethod.GET, "/api/board/comment/**")
                    .permitAll()

                    // Admin 로그인은 허용
                    .requestMatchers(HttpMethod.POST, "/api/admin/login")
                    .permitAll()

                    // 나머지 모든 요청은 인증 필요
                    .anyRequest().authenticated())

                // OIDC 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauth2UserService)
                                .oidcUserService(customOidcUserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                )

                // 필터 체인 설정
                .addFilterBefore(filterExceptionHandler,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

                return http.build();
    }
}