package com.example.golfplatform.review.controller;

import com.example.golfplatform.review.response.ReviewResponse;
import com.example.golfplatform.review.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/recent")
    public ResponseEntity<List<ReviewResponse>> getRecent(@RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(reviewService.getRecentReviews(size));
    }


}
