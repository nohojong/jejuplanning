package net.codecraft.jejutrip.security.jwt.support;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.account.user.constant.UserRole;
import net.codecraft.jejutrip.security.exception.TokenForgeryException;
import net.codecraft.jejutrip.security.jwt.domain.RefreshToken;
import net.codecraft.jejutrip.security.jwt.dto.Token;
import net.codecraft.jejutrip.security.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final CustomUserDetailService userDetailService;
    private final SecretKey secretKey;
    private final long ACCESS_TOKEN_VALID_TIME;  // = accessTokenExpirationMinutes * 60 * 1000L;
    private final long REFRESH_TOKEN_VALID_TIME; // = refreshTokenExpirationDays * 24 * 60 * 60 * 1000L;

    public JwtTokenProvider(CustomUserDetailService userDetailService,
                            @Value("${jwt.secret}") String secretKeyString,
                            @Value("${jwt.access-token-expiration-minutes}") long accessTokenExpirationMinutes,
                            @Value("${jwt.refresh-token-expiration-days}") long refreshTokenExpirationDays) {

        this.userDetailService = userDetailService;
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
        this.ACCESS_TOKEN_VALID_TIME = accessTokenExpirationMinutes * 60 * 1000L;
        this.REFRESH_TOKEN_VALID_TIME = refreshTokenExpirationDays * 24 * 60 * 60 * 1000L;
    }


    public Token createJwtToken(String userPk, UserRole roles) {

        String accessToken = createAccessToken(userPk, roles);
        String refreshToken = createRefreshToken(userPk, roles);

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .key(userPk)
                .build();
    }

    public boolean validateAccessToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtToken);

            return !claims.getPayload()
                    .getExpiration()
                    .before(new Date());

        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException e) {
            throw new TokenForgeryException("알 수 없는 토큰이거나 , 변조되었습니다.");
        }
    }

    public String validateRefreshToken(RefreshToken refreshToken) {
        String token = refreshToken.getToken();

        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)      // setSigningKey -> verifyWith
                    .build()
                    .parseSignedClaims(token);  // parseClaimsJws -> parseSignedClaims

            Claims body = claims.getPayload();  // getBody() -> getPayload()
            if (!body.getExpiration().before(new Date())) {
                return recreationAccessToken(body.getSubject(), body.get("roles"));
            }
        } catch (JwtException e) {
            return null;
        }
        return null;
    }

    public String recreationAccessToken(String userEmail, Object roles) {
            return createAccessToken(userEmail, (UserRole) roles);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserPk(token));
        if (userDetails == null) return null;
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }



    private String createAccessToken(String subject, UserRole roles) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

        return Jwts.builder()
                .subject(subject)                // setSubject -> subject
                .claim("roles", roles)         // setClaims + claims.put -> claim
                .issuedAt(now)                   // setIssuedAt -> issuedAt
                .expiration(expiration)          // setExpiration -> expiration
                .signWith(secretKey)             // signWith(SignatureAlgorithm.HS256, key) -> signWith(key)
                .compact();
    }

    private String createRefreshToken(String subject, UserRole roles) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        return Jwts.builder()
                .subject(subject)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }
}