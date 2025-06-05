package com.example.golfplatform.golfcourse.request;

public record KakaoPositionRequest(
    double lat,
    double lng,
    int radius
) {

}
