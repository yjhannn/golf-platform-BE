package com.example.golfplatform.oauth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long accessTokenValidation = 1000L * 60 * 60; // 1시간

    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + accessTokenValidation);
        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuedAt(now)
            .setExpiration(expireTime)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + (1000L * 60 * 60 * 24 * 30)); //30일
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(expireTime)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

    }
}
