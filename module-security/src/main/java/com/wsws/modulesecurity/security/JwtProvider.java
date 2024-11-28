package com.wsws.modulesecurity.security;

import com.wsws.moduledomain.auth.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider implements TokenProvider {

    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long accessExpiration;

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpiration;



    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public String createAccessToken(String userId) {
        String token = createToken(userId, accessExpiration);
        log.info("Access token 생성 userId: {}", userId);
        return token;
    }

    @Override
    public String createRefreshToken(String userId) {
        String token = createToken(userId, refreshExpiration);
        log.info("Refresh token 생성 userId: {}", userId);
        return token;
    }

    private String createToken(String userId, long expiration) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            log.info("토큰 유효성 검증 성공: {}", token);
            return true;
        } catch (Exception e) {
            log.error("토큰 유효성 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String userId = claims.getSubject();

        UserPrincipal userPrincipal = new UserPrincipal(userId);
        log.info("Authentication object created for userId: {}", userId);
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, null);
    }
}
