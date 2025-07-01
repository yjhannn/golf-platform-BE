package com.example.golfplatform.oauth.service;

import com.example.golfplatform.oauth.client.KakaoAuthClient;
import com.example.golfplatform.oauth.jwt.JwtTokenProvider;
import com.example.golfplatform.oauth.redis.RefreshTokenService;
import com.example.golfplatform.oauth.response.KakaoTokenResponse;
import com.example.golfplatform.oauth.response.KakaoUserResponse;
import com.example.golfplatform.oauth.response.TokenResponse;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final KakaoAuthClient kakaoAuthClient;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public TokenResponse kakaoLogin(String code) {
        KakaoTokenResponse kakaoToken = kakaoAuthClient.getAccessToken(code);
        KakaoUserResponse kakaoUser = kakaoAuthClient.getUserInfo(kakaoToken.accessToken());

        User user = userService.findOrCreateUser(kakaoUser);
        boolean isFirstLogin = user.isFirstLogin();

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        return new TokenResponse(accessToken, refreshToken, isFirstLogin);
    }

    public TokenResponse reissueToken(Long userId, String authHeader) {
        String refreshToken = authHeader.replace("Bearer ", "");
        // 저장된 토큰 조회
        String storedToken = refreshTokenService.getRefreshToken(userId)
            .orElseThrow(() -> new RuntimeException("리프레시 토큰이 존재하지 않습니다."));

        // 클라이언트가 보낸 refreshToken과 비교
        if(!storedToken.equals(refreshToken)) {
            throw new RuntimeException("리프레쉬 토큰이 일치하지 않습니다.");
        }

        // refreshToken 유효성 검증 (예: 만료 여부)
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("리프레시 토큰이 유효하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
        return new TokenResponse(newAccessToken, refreshToken, false);
    }

    public String kakaoUrl() {
        return kakaoAuthClient.getKakaoLoginUrl();
    }
}
