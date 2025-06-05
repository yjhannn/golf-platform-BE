package com.example.golfplatform.golfcourse.response;

public record KakaoPositionResponse(
    String address_name,
    String category_name,
    String distance,
    String phone,
    String place_name,
    String place_url,
    String road_address_name,
    String x,
    String y
) {

}
