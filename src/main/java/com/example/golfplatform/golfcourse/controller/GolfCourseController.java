package com.example.golfplatform.golfcourse.controller;

import com.example.golfplatform.golfcourse.service.GolfCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/golf-courses")
@RequiredArgsConstructor
public class GolfCourseController {
    private final GolfCourseService golfCourseService;

    @GetMapping("/nearby")
    public String getNearbyGolfCourse(@RequestParam double lat, @RequestParam double lng,
        @RequestParam int radius) {
        return golfCourseService.findNearbyGolfCourses(lat, lng, radius);
    }

    @GetMapping("/local")
    public String getLocalGolfCourse(@RequestParam String local) {
        return getLocalGolfCourse(local);
    }

}
