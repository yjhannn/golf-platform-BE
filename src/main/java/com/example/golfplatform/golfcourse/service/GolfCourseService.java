package com.example.golfplatform.golfcourse.service;

import com.example.golfplatform.golfcourse.utils.KakaoMapClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GolfCourseService {
    private final KakaoMapClient kakaoMapClient;

    public String findNearbyGolfCourses(double lat, double lng, int radius) {
        return kakaoMapClient.searchGolfCourses(lat, lng, radius);
    }

    public String findLocalGolfCourses(String local) {
        return kakaoMapClient.searchGolfCoursesByLocal(local);
    }

}
