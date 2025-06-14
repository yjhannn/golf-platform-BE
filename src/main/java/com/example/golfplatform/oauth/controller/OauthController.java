package com.example.golfplatform.oauth.controller;

import com.example.golfplatform.oauth.redis.RefreshTokenService;
import com.example.golfplatform.oauth.response.TokenResponse;
import com.example.golfplatform.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.h2.command.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth/kakao")
public class OauthController {
    private final OauthService oauthService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/login")
    public ResponseEntity<String> kakaoLogin() {
        String kakaoAuthUrl = oauthService.kakaoUrl();
        return ResponseEntity.ok(kakaoAuthUrl);
    }
    @GetMapping("/callback")
    public ResponseEntity<TokenResponse> kakaoCallBack(@RequestParam String code) {
        TokenResponse tokenResponse = oauthService.kakaoLogin(code);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Long userId) {
        refreshTokenService.deleteRefreshToken(userId);
        return ResponseEntity.ok().build();
    }
}
