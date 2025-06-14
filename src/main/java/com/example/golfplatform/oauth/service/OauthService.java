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

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public String kakaoUrl() {
        return kakaoAuthClient.getKakaoLoginUrl();
    }
}
