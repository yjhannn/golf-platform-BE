package com.example.golfplatform.golfcourse.utils;

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

    public String searchGolfCourses(double lat, double lng, int radius) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/keyword.json")
                .queryParam("query", "골프장")
                .queryParam("x", lng)
                .queryParam("y", lat)
                .queryParam("radius", radius)
                .queryParam("size", 15)
                .build())
            .header("Authorization", "KakaoAK " + apiKey)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public String searchGolfCoursesByLocal(String localName) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/keyword.json")
                .queryParam("query", localName + "골프장")
                .queryParam("size", 15)
                .build())
            .header("Authorization", "KakaoAK " + apiKey)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

}
