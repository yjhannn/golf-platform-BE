package com.example.golfplatform.oauth.redis;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(
            "refresh_token:" + userId,
            refreshToken,
            Duration.ofDays(30)
        );
    }

    public Optional<String> getRefreshToken(Long userId) {
        String token = redisTemplate.opsForValue().get("refresh_token" + userId);
        return Optional.ofNullable(token);
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete("refresh_token:" + userId);
    }

}
