package com.example.golfplatform.oauth.response;

public record KakaoUserResponse(
    Long id,
    KakaoAccount kakao_account
) {
    public record KakaoAccount(
        Profile profile
    ) {
        public record Profile(
            String nickname,
            String profile_image_url
        ) {}
    }
}