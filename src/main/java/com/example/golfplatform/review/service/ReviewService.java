package com.example.golfplatform.review.service;

import com.example.golfplatform.golfcourse.domain.GolfCourse;
import com.example.golfplatform.golfcourse.repository.GolfCourseRepository;
import com.example.golfplatform.review.domain.Review;
import com.example.golfplatform.review.repository.ReviewRepository;
import com.example.golfplatform.review.request.ReviewCreateRequest;
import com.example.golfplatform.review.response.ReviewResponse;
import com.example.golfplatform.user.domain.User;
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

    // 리뷰 작성
    @Transactional
    public ReviewResponse createReview(Long userId, ReviewCreateRequest req) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        GolfCourse course = golfCourseRepository.findById(req.golfCourseId())
            .orElseThrow(() -> new IllegalArgumentException("GolfCourse not found: " + req.golfCourseId()));

        Review review = Review.builder()
            .user(user)
            .golfCourse(course)
            .rating(req.rating())
            .title(req.title())
            .content(req.content())
            .visitedAt(req.visitedAt())
            .imageUrl(req.imageUrl())
            .build();

        Review saved = reviewRepository.save(review);
        return ReviewResponse.of(saved);
    }
}
