package com.example.golfplatform.review.service;

import com.example.golfplatform.golfcourse.repository.GolfCourseRepository;
import com.example.golfplatform.review.repository.ReviewRepository;
import com.example.golfplatform.review.response.ReviewResponse;
import com.example.golfplatform.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GolfCourseRepository golfCourseRepository;

    // 최근 리뷰 조회
    public List<ReviewResponse> getRecentReviews(int size) {
        return reviewRepository.findRecentReview(size)
            .stream()
            .map(ReviewResponse::of)
            .toList();
    }
}
