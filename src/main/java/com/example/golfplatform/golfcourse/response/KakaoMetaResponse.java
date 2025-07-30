package com.example.golfplatform.golfcourse.response;

public record KakaoMetaResponse(
    int total_count,
    int pageable_count,
    boolean is_end
) {

}
