package com.example.golfplatform.golfcourse.controller;

import com.example.golfplatform.golfcourse.request.KakaoPositionRequest;
import com.example.golfplatform.golfcourse.response.KakaoPositionResponse;
import com.example.golfplatform.golfcourse.service.GolfCourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/golf-courses")
@RequiredArgsConstructor
public class GolfCourseController {
    private final GolfCourseService golfCourseService;

    @GetMapping("/nearby")
    public ResponseEntity<List<KakaoPositionResponse>> getNearbyGolfCourse(
        @RequestParam double lat,
        @RequestParam double lng,
        @RequestParam int radius) {
        KakaoPositionRequest request = new KakaoPositionRequest(lat, lng, radius);
        List<KakaoPositionResponse> responses = golfCourseService.findNearbyGolfCourses(request);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/local")
    public String getLocalGolfCourse(@RequestParam String local) {
        return getLocalGolfCourse(local);
    }

}
