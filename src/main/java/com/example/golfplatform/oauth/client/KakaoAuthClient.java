package com.example.golfplatform.oauth.client;

import com.example.golfplatform.oauth.response.KakaoTokenResponse;
import com.example.golfplatform.oauth.response.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoAuthClient {
    private final WebClient webClient = WebClient.builder().baseUrl("https://kauth.kakao.com").build();
    private final WebClient kakaoApiWebClient = WebClient.builder().baseUrl("https://kapi.kakao.com").build();

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value(("${kakao.redirect-uri}"))
    private String redirectUri;

    public KakaoTokenResponse getAccessToken(String code) {
        return webClient.post()
            .uri("/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                .with("client_id", apiKey)
                .with("redirect_uri", redirectUri)
                .with("code", code))
            .retrieve()
            .bodyToMono(KakaoTokenResponse.class)
            .block();
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        return kakaoApiWebClient.get()
            .uri("/v2/user/me")
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(KakaoUserResponse.class)
            .block();
    }

    public String getKakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize"
            + "?client_id=" + apiKey
            + "&redirect_uri=" + redirectUri
            + "&response_type=code";
    }

}
