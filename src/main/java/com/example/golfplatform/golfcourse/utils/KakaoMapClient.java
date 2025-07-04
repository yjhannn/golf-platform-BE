package com.example.golfplatform.golfcourse.utils;

import com.example.golfplatform.golfcourse.request.KakaoLocalRequest;
import com.example.golfplatform.golfcourse.request.KakaoPositionRequest;
import com.example.golfplatform.golfcourse.response.KakaoApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoMapClient {
    @Value("${kakao.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
        .baseUrl("https://dapi.kakao.com")
        .build();

    public KakaoApiResponse searchGolfCourses(KakaoPositionRequest request) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/keyword.json")
                .queryParam("query", "골프장")
                .queryParam("x", request.lng())
                .queryParam("y", request.lat())
                .queryParam("radius", request.radius())
                .queryParam("size", 15)
                .build())
            .header("Authorization", "KakaoAK " + apiKey)
            .retrieve()
            .bodyToMono(KakaoApiResponse.class)
            .block();
    }

    public KakaoApiResponse searchGolfCoursesByLocal(KakaoLocalRequest request) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/keyword.json")
                .queryParam("query", request.Local() + "골프장")
                .queryParam("size", 15)
                .build())
            .header("Authorization", "KakaoAK " + apiKey)
            .retrieve()
            .bodyToMono(KakaoApiResponse.class)
            .block();
    }

}
