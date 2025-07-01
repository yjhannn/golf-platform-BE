package com.example.golfplatform.oauth.response;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    boolean isFirstLogin
) {

}
