package com.example.golfplatform.oauth.jwt;

import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserRepository userRepository;

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

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 유저 ID 추출
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    // 토큰에서 Authentication 생성
    public Authentication getAuthentication(String token) {
        Long userId = getUserId(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        return new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList());
    }

    // Claims 파싱
    private Claims parseClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }
}
