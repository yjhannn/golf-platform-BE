package com.example.golfplatform.review.service;

import com.example.golfplatform.golfcourse.domain.GolfCourse;
import com.example.golfplatform.golfcourse.repository.GolfCourseRepository;
import com.example.golfplatform.review.domain.Review;
import com.example.golfplatform.review.repository.ReviewRepository;
import com.example.golfplatform.review.request.ReviewCreateRequest;
import com.example.golfplatform.review.request.ReviewUpdateRequest;
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
    public ReviewResponse createReview(Long userId, ReviewCreateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        GolfCourse course = golfCourseRepository.findById(request.golfCourseId())
            .orElseThrow(() -> new IllegalArgumentException("GolfCourse not found: " + request.golfCourseId()));

        Review review = Review.builder()
            .user(user)
            .golfCourse(course)
            .rating(request.rating())
            .title(request.title())
            .content(request.content())
            .visitedAt(request.visitedAt())
            .imageUrl(request.imageUrl())
            .build();

        Review saved = reviewRepository.save(review);
        return ReviewResponse.of(saved);
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponse updateReview(Long userId, Long id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Review not found: " + id));
        if (!review.getUser().getId().equals(userId)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
        review.update(
            request.rating(),
            request.title(),
            review.getGolfCourse(),   // 골프장 변경은 불가
            request.content(),
            request.visitedAt()
        );
        return ReviewResponse.of(review);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long userId, Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Review not found: " + id));
        if (!review.getUser().getId().equals(userId)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        reviewRepository.delete(review);
    }
}
