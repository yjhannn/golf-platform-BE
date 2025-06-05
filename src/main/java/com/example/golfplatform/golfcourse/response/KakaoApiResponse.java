package com.example.golfplatform.golfcourse.response;

import java.util.List;

public record KakaoApiResponse(
    List<KakaoPositionResponse> documents
) {

}
