package com.example.golfplatform.golfcourse.utils;

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

    /** 현재 위치 기준 골프장 검색 (페이지, 사이즈 지원) */
    public KakaoApiResponse searchGolfCoursesByPosition(
        double lat, double lng, int radius,
        int page, int size
    ) {
        return webClient.get()
            .uri(uri -> uri
                .path("/v2/local/search/keyword.json")
                .queryParam("query", "골프장")
                .queryParam("x", lng)
                .queryParam("y", lat)
                .queryParam("radius", radius)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
            )
            .header("Authorization", "KakaoAK " + apiKey)
            .retrieve()
            .bodyToMono(KakaoApiResponse.class)
            .block();
    }

    /** 지역 키워드 기반 골프장 검색 (페이지, 사이즈 지원) */
    public KakaoApiResponse searchGolfCoursesByLocal(
        String local, int page, int size
    ) {
        return webClient.get()
            .uri(uri -> uri
                .path("/v2/local/search/keyword.json")
                .queryParam("query", local + "골프장")
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
            )
            .header("Authorization", "KakaoAK " + apiKey)
            .retrieve()
            .bodyToMono(KakaoApiResponse.class)
            .block();
    }
}