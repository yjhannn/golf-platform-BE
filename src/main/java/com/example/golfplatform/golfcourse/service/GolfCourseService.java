package com.example.golfplatform.golfcourse.service;

import com.example.golfplatform.golfcourse.request.KakaoLocalRequest;
import com.example.golfplatform.golfcourse.request.KakaoPositionRequest;
import com.example.golfplatform.golfcourse.response.KakaoApiResponse;
import com.example.golfplatform.golfcourse.response.KakaoPositionResponse;
import com.example.golfplatform.golfcourse.utils.KakaoMapClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GolfCourseService {
    private final KakaoMapClient kakaoMapClient;

    public List<KakaoPositionResponse> findNearbyGolfCourses(KakaoPositionRequest request) {
        KakaoApiResponse response = kakaoMapClient.searchGolfCourses(request);
        return response.documents();
    }

    public List<KakaoPositionResponse> findLocalGolfCourses(KakaoLocalRequest request) {
        String local = request.Local();
        if (local == null || local.trim().isEmpty()) {
            throw new IllegalArgumentException("지역명은 필수 입력 항목입니다.");
        }
        KakaoApiResponse response = kakaoMapClient.searchGolfCoursesByLocal(request);
        return response.documents();
    }

}
