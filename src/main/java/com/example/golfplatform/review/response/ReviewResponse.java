package com.example.golfplatform.review.response;

import com.example.golfplatform.review.domain.Review;
import java.time.format.DateTimeFormatter;

public record ReviewResponse(
    Long id,
    Long userId,
    String userName,
    Long golfCourseId,
    String golfCourseName,
    int rating,
    String title,
    String content,
    String imageUrl,
    String visitedAt,
    String createdAt
) {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static ReviewResponse of(Review r) {
        return new ReviewResponse(
            r.getId(),
            r.getUser().getId(),
            r.getUser().getNickname(),
            r.getGolfCourse().getId(),
            r.getGolfCourse().getName(),
            r.getRating(),
            r.getTitle(),
            r.getContent(),
            r.getImageUrl(),
            r.getVisitedAt().format(FMT),
            r.getCreatedAt().format(FMT)
        );
    }
}
